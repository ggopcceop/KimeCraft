package me.kime.kc.Task.ThreadTask;

import java.util.LinkedList;
import me.kime.Threadpool.ThreadPool;

public class ThreadManager extends Thread {

    private ThreadPool pool;
    private LinkedList<TTask> taskList;
    private final Byte lock;

    public ThreadManager(ThreadPool pool) {
        this.lock = Byte.MAX_VALUE;
        this.pool = pool;
        taskList = new LinkedList<>();
    }

    public void registerTask(TTask task) {
        taskList.add(task);
    }

    @Override
    public void run() {
        boolean hasTask;
        while (true) {
            synchronized (lock) {
                hasTask = false;
                for (TTask task : taskList) {
                    int taskSize = task.queueSize();
                    for (int i = 0; i < taskSize; i++) {
                        //hasTask = true;
                        pool.addTask(task);
                    }
                }
            }
            if (!hasTask) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isDone() {
        synchronized (lock) {
            for (TTask task : taskList) {
                if (task.queueSize() > 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
