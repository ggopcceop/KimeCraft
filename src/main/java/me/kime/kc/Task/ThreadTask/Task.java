package me.kime.kc.Task.ThreadTask;

public abstract class Task implements Runnable {

    protected final byte[] lock = new byte[0];

    public abstract int queueSize();
}
