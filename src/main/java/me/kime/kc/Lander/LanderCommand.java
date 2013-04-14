package me.kime.kc.Lander;

import me.kime.kc.Util.KCMessager;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanderCommand implements CommandExecutor {

    private Lander lander;

    public LanderCommand(Lander lander) {
        this.lander = lander;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Location loc = player.getLocation();

        if (lander.getPlugin().getDefaultWorld() != loc.getWorld()) {
            return true;
        }

        KCMessager.sentMessage(player, "current chunk: "
                + player.getLocation().getChunk().getX() + " "
                + player.getLocation().getChunk().getZ(), ChatColor.BLUE);
        if (split.length == 1) {
            if ("add".equalsIgnoreCase(split[0])) {
                Chunk chunk = player.getLocation().getChunk();
                lander.addLand(chunk.getX(), chunk.getZ(), player.getName(),
                        player.getName() + "'s Land");
            }
        }
        KLand kLand = lander.getKChunkByOwner(player.getName());
        if (kLand != null) {
            KCMessager.sentMessage(player, kLand.getOwner() + " " + kLand.getName()
                    + " " + kLand.getChunks().size(), ChatColor.GREEN);
        }


        return true;
    }
}
