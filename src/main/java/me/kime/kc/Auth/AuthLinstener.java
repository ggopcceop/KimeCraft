package me.kime.kc.Auth;

import me.kime.kc.KPlayer;
import me.kime.kc.Util.KCMessager;

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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;

/**
 * stop player action when non-auth
 *
 * @author Kime
 *
 */
public class AuthLinstener implements Listener {

    private Auth auth;

    public AuthLinstener(Auth instance) {
        this.auth = instance;
    }

    //Cant move
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        KPlayer kPlayer = auth.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth()) {
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
        KPlayer kPlayer = auth.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth()) {
            if (event.getMessage().startsWith("/login")) {
                return;
            }
            KCMessager.sentMessage(kPlayer.getPlayer(), "Type '/login [password]' to login", ChatColor.GREEN);
            event.setCancelled(true);
        }
    }

    //Cant drop item
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        KPlayer kPlayer = auth.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth()) {
            event.setCancelled(true);
        }
    }

    //cant pick up item
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        KPlayer kPlayer = auth.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth()) {
            event.setCancelled(true);
        }
    }

    //cant interact
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        KPlayer kPlayer = auth.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth()) {
            event.setCancelled(true);
        }
    }

    //cant interact entity
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        KPlayer kPlayer = auth.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth()) {
            event.setCancelled(true);
        }
    }

    //cant chat
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        KPlayer kPlayer = auth.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth()) {
            KCMessager.sentMessage(kPlayer.getPlayer(), "Type '/login [password]' to login", ChatColor.GREEN);
            event.setCancelled(true);
        }
    }

    //cant portal
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPortal(PlayerPortalEvent event) {
        KPlayer kPlayer = auth.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer == null) {
            event.setCancelled(true);
            return;
        }
        if (!kPlayer.isAuth()) {
            Location loc = kPlayer.getPlayer().getLocation();
            loc.setY(256);
            kPlayer.getPlayer().teleport(loc);
            event.setCancelled(true);
        }
    }

    //cant be damage
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            KPlayer kPlayer = auth.getOnlinePlayer(player.getName());
            if (kPlayer == null) {
                event.setCancelled(true);
                return;
            }
            if (!kPlayer.isAuth()) {
                event.setCancelled(true);
            }
        }
    }

    //cant be damage by entity
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityByEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            KPlayer kPlayer = auth.getOnlinePlayer(player.getName());
            if (kPlayer == null) {
                event.setCancelled(true);
                return;
            }
            if (!kPlayer.isAuth()) {
                event.setCancelled(true);
            }
        }
    }

    //food level will no change
    @EventHandler(priority = EventPriority.LOWEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            KPlayer kPlayer = auth.getOnlinePlayer(player.getName());
            if (kPlayer == null) {
                event.setCancelled(true);
                return;
            }
            if (!kPlayer.isAuth()) {
                event.setCancelled(true);
            }
        }
    }

    //entity will not target non-auth player
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            KPlayer kPlayer = auth.getOnlinePlayer(player.getName());
            if (kPlayer == null) {
                event.setCancelled(true);
                return;
            }
            if (!kPlayer.isAuth()) {
                event.setCancelled(true);
            }
        }
    }
}
