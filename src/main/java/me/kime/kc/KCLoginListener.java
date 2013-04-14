package me.kime.kc;

import java.util.HashMap;

import me.kime.kc.Task.AuthTimeoutTask;
import me.kime.kc.Task.BorderCheckTask;
import me.kime.kc.Task.ThreadTask.LoginSQLTask;
import me.kime.kc.Task.ThreadTask.SessionSQLTask;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Login listener act when player login
 *
 * @author Kime
 *
 */
public class KCLoginListener implements Listener {

    private KC plugin;
    private LoginSQLTask loginSQLTask;
    private SessionSQLTask sessionSQLTask;
    private HashMap<String, KPlayer> onlineList;

    public KCLoginListener(KC instance, HashMap<String, KPlayer> onlineList) {
        plugin = instance;
        this.onlineList = onlineList;

        loginSQLTask = new LoginSQLTask(plugin);
        sessionSQLTask = new SessionSQLTask(plugin.getAuth());

        plugin.registerTask(loginSQLTask);
        plugin.registerTask(sessionSQLTask);
    }

    /**
     * Kick illegal name or name length
     *
     * @param event PlayerPreLoginEvent
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String name = event.getPlayer().getName();
        if (plugin.getOnlinePlayer(name) != null) {
            event.disallow(Result.KICK_OTHER, "Player already online! If you are not playing, please contact admin");
        }
        int min = 3;
        int max = 16;
        String regex = "[a-zA-Z0-9_]+";

        if (name.length() > max || name.length() < min) {
            event.disallow(Result.KICK_OTHER, "Your nickname has the wrong length. MaxLen: " + max + ", MinLen: " + min);
            return;
        }
        if (!name.matches(regex) || name.equals("Player")) {
            event.disallow(Result.KICK_OTHER, "Your nickname contains illegal characters. Allowed chars: " + regex);
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
        Player player = event.getPlayer();

        KPlayer kPlayer = new KPlayer(player);
        onlineList.put(player.getName().toLowerCase(), kPlayer);

        //add border check task
        int borderCheckTaskId = plugin.getServer().getScheduler()
                .scheduleSyncRepeatingTask(plugin, new BorderCheckTask(kPlayer, plugin.getFun().sRR), 100L, 100L);
        kPlayer.setBorderCheckTaskId(borderCheckTaskId);

        kPlayer.cache();

        loginSQLTask.queue(kPlayer);

        //timeout task for each login player
        int timeout = 120 * 20;
        int timeoutTaskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new AuthTimeoutTask(plugin.getAuth(), player.getName()), timeout);

        kPlayer.setTimeoutTaskId(timeoutTaskId);

        if (player.getLastPlayed() == 0) {
            final String name = player.getName();
            event.setJoinMessage(ChatColor.GREEN + "New player " + ChatColor.YELLOW + name + ChatColor.GREEN + " joined the server!");

            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    KPlayer kp = plugin.getOnlinePlayer(name);
                    if (kp.getPlayer().isOnline()) {
                        kp.getPlayer().teleport(plugin.getNoob().getNoobWorld().getSpawnLocation());
                        kp.getPlayer().setGameMode(GameMode.CREATIVE);
                    }
                }
            }, 10L);
        }
    }

    /**
     * Resote all player inventory and status for non-auth player when player
     * quit Cancel timeout kick task too
     *
     * @param event PlayerQuitEvent
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        KPlayer kPlayer = onlineList.remove(event.getPlayer().getName().toLowerCase());
        if (kPlayer == null) {
            return;
        }

        //cancel border check task
        int id = kPlayer.getBorderCheckTaskId();
        plugin.getServer().getScheduler().cancelTask(id);
        
        if (!kPlayer.isAuth()) {
            kPlayer.restoreCache();
            if (kPlayer.getTimeoutTaskId() != -1) {
                plugin.getServer().getScheduler().cancelTask(kPlayer.getTimeoutTaskId());
            }
        } else {
            sessionSQLTask.queue(kPlayer);
        }
    }
}
