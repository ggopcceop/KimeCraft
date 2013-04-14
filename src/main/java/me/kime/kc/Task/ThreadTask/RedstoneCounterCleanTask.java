package me.kime.kc.Task.ThreadTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import me.kime.kc.Fun.Fun;
import me.kime.kc.Fun.RedstoneC;

import org.bukkit.Location;

public class RedstoneCounterCleanTask extends TTask {

    private Fun fun;
    private int count = 0;
    private final Byte lock;

    public RedstoneCounterCleanTask(Fun fun) {
        this.lock = Byte.MAX_VALUE;
        this.fun = fun;
    }

    @Override
    public void run() {
        count = 0;
        synchronized (lock) {
            HashMap<Location, RedstoneC> list = fun.getRedstone();
            Iterator<Location> locs = list.keySet().iterator();
            LinkedList<Location> removeList = new LinkedList<>();
            long time = System.currentTimeMillis();

            while (locs.hasNext()) {
                Location loc = locs.next();
                RedstoneC c = list.get(loc);
                if ((time - c.getTime()) > 120000) {
                    removeList.add(loc);
                }
            }
            while (!removeList.isEmpty()) {
                Location loc = removeList.removeFirst();
                list.remove(loc);
            }
        }
    }

    public void queue() {
        count++;
    }

    @Override
    public int queueSize() {
        return count;
    }
}
