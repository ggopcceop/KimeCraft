package me.kime.kc.task.threadTask;

import java.util.LinkedList;
import me.kime.kc.KPlayer;
import me.kime.kc.auth.Auth;
import me.kime.kc.auth.AuthSucceedEvent;
import me.kime.kc.util.KMessager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LoginSQLTask extends Task {

    private final Auth addon;
    private final LinkedList<KPlayer> list;

    public LoginSQLTask(Auth instance) {
        this.list = new LinkedList<>();
        this.addon = instance;
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

        addon.getDataSource().getAuth(kPlayer);

        long diffTime = System.currentTimeMillis() - kPlayer.LoginDate;

        if (diffTime <= addon.getSessionTime()
                && kPlayer.LoginIp.equals(kPlayer.player.getAddress().getHostString())
                && (kPlayer.groupId < 4 || kPlayer.groupId > 9)) {

            kPlayer.restoreCache();
            kPlayer.isAuth = true;

            //call motd event
            Bukkit.getPluginManager().callEvent(new AuthSucceedEvent(kPlayer));
        } else {
            KMessager.sendMessage(kPlayer, ChatColor.GREEN, "auth_AskLogin");
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
