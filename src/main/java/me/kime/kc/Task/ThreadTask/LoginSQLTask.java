package me.kime.kc.Task.ThreadTask;

import java.util.LinkedList;

import org.bukkit.ChatColor;

import me.kime.kc.KC;
import me.kime.kc.KPlayer;
import me.kime.kc.Util.KCMessager;

public class LoginSQLTask extends Task {

    private KC plugin;
    private LinkedList<KPlayer> list;

    public LoginSQLTask(KC instance) {
        this.list = new LinkedList<>();
        this.plugin = instance;
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
        plugin.getAuth().getDataSource().getAuth(kPlayer);

        long diffTime = System.currentTimeMillis() - kPlayer.getLoginDate();

        if (diffTime <= plugin.getAuth().getSessionTime()
                && kPlayer.getLoginIp().equals(kPlayer.getPlayer().getAddress().getHostString())
                && (kPlayer.getGroup() < 4 || kPlayer.getGroup() > 9)) {

            kPlayer.restoreCache();
            kPlayer.setAuth(true);
        } else {
            KCMessager.sentMessage(kPlayer.getPlayer(), "Type '/login [password]' to login", ChatColor.GREEN);
        }

    }

    public void queue(KPlayer kPlayer) {
        list.addLast(kPlayer);
    }

    @Override
    public int queueSize() {
        return list.size();
    }
}
