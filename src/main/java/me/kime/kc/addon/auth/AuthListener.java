/*
 * Copyright (C) 2014 Kime
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.kime.kc.addon.auth;

import me.kime.kc.KPlayer;
import me.kime.kc.task.AuthTimeoutTask;
import me.kime.kc.util.KMessager;
import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * stop player action when non-auth
 *
 * @author Kime
 *
 */
public class AuthListener implements Listener {

    private final Auth addon;

    public AuthListener(Auth instance) {
        this.addon = instance;
    }

    /**
     * Kick illegal name or name length
     *
     * @param event PlayerPreLoginEvent
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String name = event.getPlayer().getName();
        if (addon.getOnlinePlayer(name) != null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Player already online! If you are not playing, please contact admin");
            return;
        }
        int min = 3;
        int max = 16;
        String regex = "[a-zA-Z0-9_]+";

        if (name.length() > max || name.length() < min) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your nickname has the wrong length. MaxLen: " + max + ", MinLen: " + min);
            return;
        }
        if (!name.matches(regex) || name.equals("Player")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your nickname contains illegal characters. Allowed chars: " + regex);
            return;
        }

        //add hard player limit to server at 200
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {
            if (addon.getPlugin().getServer().getOnlinePlayers().length <= 200) {
                event.allow();
            }
        }
    }

    /**
     * Cache player inventory and status to auth player. ask password hash from
     * database. Start a non-auth timeout task.
     *
     * @param event PlayerJoinEvent
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        String user = kPlayer.player.getName().toLowerCase();

        kPlayer.cache();

        addon.getDataSource().query("SELECT pre_ucenter_members.username, "
                + "pre_ucenter_members.password, pre_ucenter_members.salt, "
                + "pre_common_member.groupid, pre_ucenter_members.lloginip, "
                + "pre_ucenter_members.llogindate FROM pre_ucenter_members,"
                + " pre_common_member  WHERE pre_ucenter_members.username=? "
                + "AND pre_common_member.username=?;", pst -> {
                    pst.setString(1, user);
                    pst.setString(2, user);

                }).onDone(rs -> {
                    if (rs.next()) {
                        if (rs.getString("lloginip") == null || rs.getString("lloginip").isEmpty()) {
                            kPlayer.LoginIp = "198.18.0.1";
                        } else {
                            kPlayer.LoginIp = rs.getString("lloginip");
                        }

                        kPlayer.password = rs.getString("password");
                        kPlayer.salt = rs.getString("salt");
                        kPlayer.groupId = rs.getInt("groupid");
                        kPlayer.LoginDate = rs.getLong("llogindate");
                    }

                    long diffTime = System.currentTimeMillis() - kPlayer.LoginDate;

                    if (diffTime <= addon.getSessionTime()
                    && kPlayer.LoginIp.equals(kPlayer.player.getAddress().getHostString())
                    && (kPlayer.groupId < 4 || kPlayer.groupId > 9)) {

                        kPlayer.restoreCache();
                        kPlayer.isAuth = true;

                        //call motd event
                        Bukkit.getPluginManager().callEvent(new AuthSucceedEvent(kPlayer));
                    } else {
                        KMessager.sendMessage(kPlayer, ChatColor.GREEN, "auth_AskLogin");
                    }
                }).execute();

        //timeout task for each login player
        int timeout = 120 * 20;
        int timeoutTaskId = addon.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(
                addon.getPlugin(), new AuthTimeoutTask(addon, kPlayer.player.getName()), timeout);

        kPlayer.setTimeoutTaskId(timeoutTaskId);
    }

    /**
     * Resote all player inventory and status for non-auth player when player
     * quit Cancel timeout kick task too
     *
     * @param event PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            return;
        }

        if (!kPlayer.isAuth) {
            kPlayer.restoreCache();
            if (kPlayer.getTimeoutTaskId() != -1) {
                addon.getPlugin().getServer().getScheduler().cancelTask(kPlayer.getTimeoutTaskId());
            }
        } else {
            addon.getDataSource().update("UPDATE pre_ucenter_members SET lloginip = ?,"
                    + " llogindate = ? WHERE username = ?;", pst -> {
                        pst.setString(1, kPlayer.player.getAddress().getHostString());
                        pst.setLong(2, System.currentTimeMillis());
                        pst.setString(3, kPlayer.getNameLowCase());
                    }).execute();
        }
    }

    //Cant move
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getX() == to.getX() && from.getZ() == to.getZ()) {
                return;
            }
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth) {
            if (event.getMessage().startsWith("/login ")) {
                return;
            } else if (event.getMessage().startsWith("/l ")) {
                return;
            }
            KMessager.sendMessage(kPlayer, ChatColor.GREEN, "auth_AskLogin");
            event.setCancelled(true);
        }
    }

    //Cant drop item
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth) {
            event.setCancelled(true);
        }
    }

    //cant pick up item
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth) {
            event.setCancelled(true);
        }
    }

    //cant interact
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth) {
            event.setCancelled(true);
        }
    }

    //cant interact entity
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth) {
            event.setCancelled(true);
        }
    }

    //cant chat
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth) {
            KMessager.sendMessage(kPlayer, ChatColor.GREEN, "auth_AskLogin");
            event.setCancelled(true);
        }
    }

    //cant portal
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        KPlayer kPlayer = addon.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth) {
            Location loc = kPlayer.player.getLocation();
            loc.setY(256);
            kPlayer.player.teleport(loc);
            event.setCancelled(true);
        }
    }

    //cant be damage
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            KPlayer kPlayer = addon.getOnlinePlayer(player.getName());
            if (kPlayer == null) {
                event.setCancelled(true);
                return;
            }
            if (!kPlayer.isAuth) {
                event.setCancelled(true);
            }
        }
    }

    //cant be damage by entity
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityByEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            KPlayer kPlayer = addon.getOnlinePlayer(player.getName());
            if (kPlayer == null) {
                event.setCancelled(true);
                return;
            }
            if (!kPlayer.isAuth) {
                event.setCancelled(true);
            }
        }
    }

    //food level will no change
    @EventHandler(priority = EventPriority.LOWEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            KPlayer kPlayer = addon.getOnlinePlayer(player.getName());
            if (kPlayer == null) {
                event.setCancelled(true);
                return;
            }
            if (!kPlayer.isAuth) {
                event.setCancelled(true);
            }
        }
    }

    //entity will not target non-auth player
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            KPlayer kPlayer = addon.getOnlinePlayer(player.getName());
            if (kPlayer == null) {
                event.setCancelled(true);
                return;
            }
            if (!kPlayer.isAuth) {
                event.setCancelled(true);
            }
        }
    }
}
