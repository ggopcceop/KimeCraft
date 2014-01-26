package me.kime.kc.task.threadTask;

public abstract class Task implements Runnable {

    protected final byte[] lock = new byte[0];

    public abstract int queueSize();
}
