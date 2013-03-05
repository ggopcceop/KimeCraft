package me.Kime.KC.Task.ThreadTask;

import java.util.LinkedList;

import me.Kime.KC.KPlayer;
import me.Kime.KC.Auth.Auth;

public class SessionSQLTask extends TTask {

    private Auth plugin;
    private LinkedList<KPlayer> list;
    private final Byte lock;

    public SessionSQLTask(Auth auth) {
        this.lock = Byte.MAX_VALUE;
        this.list = new LinkedList<>();
        this.plugin = auth;
    }

    @Override
    public void run() {
        KPlayer kPlayer;

        synchronized (lock) {
            if (!list.isEmpty()) {
                kPlayer = list.removeFirst();
            } else {
                return;
            }
        }
        plugin.getDataSource().updateSession(kPlayer);
    }

    public void queue(KPlayer kPlayer) {
        list.addLast(kPlayer);
    }

    @Override
    public int queueSize() {
        return list.size();
    }
}
