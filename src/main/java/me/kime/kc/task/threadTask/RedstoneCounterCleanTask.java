package me.kime.kc.task.threadTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import me.kime.kc.fun.Fun;
import me.kime.kc.fun.RedstoneC;

import org.bukkit.Location;

public class RedstoneCounterCleanTask extends Task {

    private final Fun fun;
    private int count = 0;

    public RedstoneCounterCleanTask(Fun fun) {
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
