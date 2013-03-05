package me.Kime.KC;

import me.Kime.KC.Util.KCMessager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KCCommand implements CommandExecutor {

    private KC plugin;

    public KCCommand(KC instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        World world = player.getLocation().getWorld();
        if (world.equals(plugin.getDefaultWorld()) || world.equals(plugin.getMine().getMineWorld())) {
            Location loc = new Location(plugin.getDefaultWorld(), -308, 216, 22, 0, 0);
            player.getPlayer().teleport(loc);
            KCMessager.sentMessage(player, "You are in City!", ChatColor.YELLOW);
        } else {
            KCMessager.sentMessage(player, "You can only go back to city from main or mine world!", ChatColor.RED);
        }
        return true;
    }
}
