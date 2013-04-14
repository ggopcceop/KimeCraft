package me.kime.kc.Task.ThreadTask;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.kime.kc.KC;
import me.kime.kc.KPlayer;
import me.kime.kc.Util.KCMessager;

public class MinePaymentTask extends TTask {

    private int count;
    private KC plugin;
    private final Byte lock;
    DecimalFormat format;

    public MinePaymentTask(KC instance) {
        this.lock = Byte.MAX_VALUE;
        this.plugin = instance;
        format = new DecimalFormat("#.##");
    }

    @Override
    public void run() {
        count = 0;
        synchronized (lock) {
            Player[] players = plugin.getServer().getOnlinePlayers();
            for (int i = 0; i < players.length; i++) {
                KPlayer player = plugin.getOnlinePlayer(players[i].getName());
                if (player.getSalary(false) > 0) {
                    double mount = player.getSalary(true);
                    plugin.getEconomy().depositPlayer(players[i].getName(), mount);
                    if (mount >= 1) {
                        KCMessager.sentMessage(players[i], "You earned $" + format.format(mount), ChatColor.DARK_AQUA);
                    }

                }
            }
        }
    }

    @Override
    public int queueSize() {
        return count;
    }

    public void queue() {
        count++;
    }
}
