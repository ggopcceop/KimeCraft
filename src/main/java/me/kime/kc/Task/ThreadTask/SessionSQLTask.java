package me.kime.kc.Task.ThreadTask;

import java.util.LinkedList;

import me.kime.kc.KPlayer;
import me.kime.kc.Auth.Auth;

public class SessionSQLTask extends Task {

    private Auth plugin;
    private LinkedList<KPlayer> list;

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
