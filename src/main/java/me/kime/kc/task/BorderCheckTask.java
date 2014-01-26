package me.kime.kc.task;

import me.kime.kc.KPlayer;
import me.kime.kc.util.KMessager;
import org.bukkit.Chunk;

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
            KMessager.sendError(player, "fun_readchBorder");
        } else {
            player.setLastLoc();
        }

    }
}
