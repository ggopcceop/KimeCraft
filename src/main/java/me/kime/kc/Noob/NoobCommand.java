package me.kime.kc.Noob;

import me.kime.kc.KPlayer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoobCommand implements CommandExecutor {

    private Noob noob;

    public NoobCommand(Noob noob) {
        this.noob = noob;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (player.hasPermission("kc.admin.noob")) {
            if ("pd".equalsIgnoreCase(command.getName())) {
                if (split.length == 1) {
                    KPlayer p = noob.getPlugin().getOnlinePlayer(split[0]);
                    if (p != null) {
                        if (p.isAuth()) {
                            p.getPlayer().teleport(noob.getNoobWorld().getSpawnLocation());
                        } else {
                            sender.sendMessage(ChatColor.RED + split[0] + " is not login");
                        }
                        return true;
                    }
                    player.sendMessage(ChatColor.RED + split[0] + " not found");
                    return true;
                }
            } else if ("pu".equalsIgnoreCase(command.getName())) {
                if (split.length == 1) {
                    KPlayer p = noob.getPlugin().getOnlinePlayer(split[0]);
                    if (p != null) {
                        if (p.isAuth()) {
                            Location loc = new Location(noob.getPlugin().getDefaultWorld(), -308, 216, 22, 0, 0);
                            p.getPlayer().teleport(loc);
                            
                            loc = new Location(noob.getPlugin().getDefaultWorld(), -308, 216, 30, 0, 0);
                            //play firework to welcome noobs
                            noob.playFirework(loc, 10);
                            
                            player.sendMessage(ChatColor.YELLOW + p.getPlayer().getName() + " is in City now!");
                        } else {
                            sender.sendMessage(ChatColor.RED + split[0] + " is not login");
                        }
                        return true;
                    }
                    player.sendMessage(ChatColor.RED + split[0] + " not found");
                    return true;
                }
            }
        }
        return false;
    }
}
