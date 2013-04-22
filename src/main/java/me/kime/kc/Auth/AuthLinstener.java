package me.kime.kc.Auth;

import java.util.HashMap;
import me.kime.kc.KPlayer;
import me.kime.kc.Task.AuthTimeoutTask;
import me.kime.kc.Task.ThreadTask.LoginSQLTask;
import me.kime.kc.Task.ThreadTask.SessionSQLTask;
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
public class AuthLinstener implements Listener {

    private final Auth auth;
    private final HashMap<String, KPlayer> onlineList;
    private final SessionSQLTask sessionSQLTask;
    private final LoginSQLTask loginSQLTask;

    public AuthLinstener(Auth instance, HashMap<String, KPlayer> onlineList) {
        this.auth = instance;
        this.onlineList = onlineList;

        loginSQLTask = new LoginSQLTask(auth.getPlugin());
        sessionSQLTask = new SessionSQLTask(auth);

        auth.getPlugin().registerTask(loginSQLTask);
        auth.getPlugin().registerTask(sessionSQLTask);
    }

    /**
     * Kick illegal name or name length
     *
     * @param event PlayerPreLoginEvent
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String name = event.getPlayer().getName();
        if (onlineList.get(name) != null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Player already online! If you are not playing, please contact admin");
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
        }
    }

    /**
     * Cache player inventory and status to auth player. ask password hash from
     * database. Start a non-auth timeout task.
     *
     * @param event PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        KPlayer kPlayer = new KPlayer(player);
        onlineList.put(player.getName().toLowerCase(), kPlayer);

        kPlayer.cache();

        loginSQLTask.queue(kPlayer);

        //timeout task for each login player
        int timeout = 120 * 20;
        int timeoutTaskId = auth.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(
                auth.getPlugin(), new AuthTimeoutTask(auth.getPlugin().getAuth(), player.getName()), timeout);

        kPlayer.setTimeoutTaskId(timeoutTaskId);
    }

    /**
     * Resote all player inventory and status for non-auth player when player
     * quit Cancel timeout kick task too
     *
     * @param event PlayerQuitEvent
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        KPlayer kPlayer = onlineList.remove(event.getPlayer().getName().toLowerCase());
        if (kPlayer == null) {
            return;
        }

        if (!kPlayer.isAuth()) {
            kPlayer.restoreCache();
            if (kPlayer.getTimeoutTaskId() != -1) {
                auth.getPlugin().getServer().getScheduler().cancelTask(kPlayer.getTimeoutTaskId());
            }
        } else {
            sessionSQLTask.queue(kPlayer);
        }
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
