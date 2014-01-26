package me.kime.kc.task.threadTask;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.kime.kc.KimeCraft;
import me.kime.kc.KPlayer;
import me.kime.kc.util.KMessager;
import net.milkbowl.vault.economy.Economy;

public class MinePaymentTask extends Task {

    private int count;
    private final KimeCraft plugin;
    DecimalFormat format;

    public MinePaymentTask(KimeCraft instance) {
        this.plugin = instance;
        format = new DecimalFormat("#.##");
    }

    @Override
    public void run() {
        count = 0;
        synchronized (lock) {
            Player[] players = plugin.getServer().getOnlinePlayers();
            for (Player player1 : players) {
                KPlayer player = plugin.getOnlinePlayer(player1.getName());
                if (player.getSalary(false) > 0) {
                    double mount = player.getSalary(true);
                    Economy eco = plugin.getEconomy();
                    if (eco != null) {
                        eco.depositPlayer(player1.getName(), mount);
                    }
                    if (mount >= 1) {
                        KMessager.sendMessage(player, ChatColor.AQUA, "mine_salaryEarned", format.format(mount));
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
