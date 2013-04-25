/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kime.kc.Task;

import java.util.LinkedList;
import me.kime.kc.Task.ThreadTask.TTask;

/**
 *
 * @author Kime
 */
public class KSyncTask implements Runnable {

    private final LinkedList<TTask> list;
    private final Byte lock;

    public KSyncTask() {
        list = new LinkedList();
        this.lock = Byte.MAX_VALUE;
    }

    @Override
    public void run() {
        synchronized (lock) {
            if (!list.isEmpty()) {
                TTask task = list.remove();
                task.run();
            }
        }
    }

    public void queue(TTask task) {
        synchronized (lock) {
            list.add(task);
        }

    }
}
