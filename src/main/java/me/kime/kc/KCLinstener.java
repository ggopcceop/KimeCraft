package me.kime.kc;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Kime
 */
public class KCLinstener implements Listener {

    private final KimeCraft plugin;
    private final HashMap<String, KPlayer> onlineList;

    KCLinstener(KimeCraft instance, HashMap<String, KPlayer> onlineList) {
        this.plugin = instance;
        this.onlineList = onlineList;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        KPlayer kPlayer = new KPlayer(player);
        onlineList.put(player.getName().toLowerCase(), kPlayer);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        onlineList.remove(event.getPlayer().getName().toLowerCase());
    }
}
