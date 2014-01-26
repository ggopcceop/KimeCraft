/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kime.kc.task;

import java.util.Random;
import me.kime.kc.util.KFireworkMarker;
import org.bukkit.Location;

/**
 *
 * @author Kime
 */
public class FireworkDelayTask implements Runnable {
    private final Random rnd;
    private final Location loc;

    public FireworkDelayTask(Random rnd, Location loc) {
        this.rnd = rnd;
        this.loc = loc;
    }

    @Override
    public void run() {
        KFireworkMarker.playRandomFirework(loc.getWorld(), loc, rnd);
    }
}
