package me.kime.kc;

import java.util.HashMap;
import me.kime.kc.Util.KCMessager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class KCCommand implements CommandExecutor {
    
    private final KimeCraft plugin;
    private final long cooldown = 30 * 60 * 1000;
    private final HashMap<String, CD> cooldownData;
    
    public KCCommand(KimeCraft instance) {
        this.plugin = instance;
        cooldownData = new HashMap<>();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        World world = player.getLocation().getWorld();
        CD cd = cooldownData.get(player.getName().toLowerCase());
        if (cd == null) {
            cd = new CD();
            cooldownData.put(player.getName().toLowerCase(), cd);
        }
        
        long time = System.currentTimeMillis() - cd.getCityCD();
        if (time >= cooldown) {
            if (world.equals(plugin.getDefaultWorld()) || world.equals(plugin.getMine().getMineWorld())) {
                Location loc = new Location(plugin.getDefaultWorld(), -308, 216, 22, 0, 0);
                player.getPlayer().teleport(loc, TeleportCause.COMMAND);
                KCMessager.sentMessage(player, "You are in City!", ChatColor.YELLOW);
                cd.setCityCD(System.currentTimeMillis());
            } else {
                KCMessager.sentMessage(player, "You can only go back to city from main or mine world!", ChatColor.RED);
            }
        } else {
            KCMessager.sentMessage(player, "City cooldown in " + ((cooldown - time) / 1000) + " second!", ChatColor.RED);
        }
        
        return true;
    }
    
    class CD {
        
        private long city_cd = 0;
        
        public long getCityCD() {
            return this.city_cd;
        }
        
        public void setCityCD(long cd) {
            this.city_cd = cd;
        }
    }
}
