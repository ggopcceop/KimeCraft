package me.kime.kc.task.threadTask;

import java.util.LinkedList;

import me.kime.kc.KPlayer;
import me.kime.kc.auth.Auth;

public class SessionSQLTask extends Task {

    private final Auth plugin;
    private final LinkedList<KPlayer> list;

    public SessionSQLTask(Auth auth) {
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
