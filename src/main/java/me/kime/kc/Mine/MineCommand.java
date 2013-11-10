package me.kime.kc.Mine;

import java.text.DecimalFormat;

import me.kime.kc.KPlayer;
import me.kime.kc.Util.KCMessager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class MineCommand implements CommandExecutor {

    private Mine mine;
    private DecimalFormat format;

    public MineCommand(Mine mine) {
        this.mine = mine;
        format = new DecimalFormat("#.##");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (split.length == 0) {
            if (player.getWorld().equals(mine.getPlugin().getDefaultWorld())) {
                player.teleport(mine.getMineWorld().getSpawnLocation(), TeleportCause.COMMAND);
                KCMessager.sentMessage(player, "welcome to mine world, this is a temp world for mining olny", ChatColor.GREEN);
            } else {
                KCMessager.sentMessage(player, "You can only enter mine world from the main wolrd", ChatColor.RED);
            }
            return true;
        } else if (split.length == 1) {
            if ("salary".equalsIgnoreCase(split[0])) {
                KPlayer kPlayer = mine.getPlugin().getOnlinePlayer(player.getName());
                KCMessager.sentMessage(player, "You earned $" + format.format(kPlayer.getTotalSalary())
                        + " since login", ChatColor.BLUE);
                return true;
            }

        }

        KCMessager.sentError(player, "Usage: /mine or /mine salary");
        return true;
    }
}
