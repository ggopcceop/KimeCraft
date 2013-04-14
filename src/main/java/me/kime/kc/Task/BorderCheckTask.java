package me.kime.kc.Task;

import me.kime.kc.KPlayer;
import me.kime.kc.Util.KCMessager;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * check player location to prevent player go out of the border
 *
 * @author Kime
 */
public class BorderCheckTask implements Runnable {

    private final KPlayer player;
    private final int sRR;

    public BorderCheckTask(KPlayer kPlayer, int srr) {
        this.player = kPlayer;
        this.sRR = srr;
    }

    @Override
    public void run() {
        Chunk chunk = player.getPlayer().getLocation().getChunk();
        int x = chunk.getX();
        int z = chunk.getZ();
        if (((x * x) + (z * z)) > sRR) {
            player.tpLastLoc();
            KCMessager.sentMessage(player , "You reached border! ", ChatColor.RED);
        } else {
            player.setLastLoc();
        }

    }
}
