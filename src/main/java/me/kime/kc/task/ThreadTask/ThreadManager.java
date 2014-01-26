package me.kime.kc.task.threadTask;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import me.kime.kc.util.KLogger;

public class ThreadManager extends Thread {
    
    private Executor pool;
    private LinkedList<Task> taskList;
    private final Byte lock;
    
    public ThreadManager(Executor pool) {
        this.lock = Byte.MAX_VALUE;
        this.pool = pool;
        taskList = new LinkedList<>();
    }
    
    public void registerTask(Task task) {
        taskList.add(task);
    }
    
    @Override
    public void run() {
        boolean hasTask;
        while (true) {
            synchronized (lock) {
                hasTask = false;
                for (Task task : taskList) {
                    int taskSize = task.queueSize();
                    for (int i = 0; i < taskSize; i++) {
                        //hasTask = true;
                        pool.execute(task);
                    }
                }
            }
            if (!hasTask) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    KLogger.showError(e.getMessage());
                }
            }
        }
    }
    
    public boolean isDone() {
        synchronized (lock) {
            for (Task task : taskList) {
                if (task.queueSize() > 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
