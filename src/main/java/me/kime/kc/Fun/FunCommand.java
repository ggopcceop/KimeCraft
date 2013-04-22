package me.kime.kc.Fun;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class FunCommand implements CommandExecutor {

    private final Fun fun;

    public FunCommand(Fun fun) {
        this.fun = fun;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if ("skull".equalsIgnoreCase(label)) {
            if (player.isOp() && (split.length == 1)) {
                ItemStack item = player.getItemInHand();
                if (item.getTypeId() == 397) {
                    SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                    skullMeta.setOwner(split[0]);
                    item.setItemMeta(skullMeta);
                }
            }
            return true;
        } else if ("roll".equalsIgnoreCase(label)) {
            int num = fun.getPlugin().getRandom().nextInt(99) + 1;
            fun.getPlugin().getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + " rolled " + num);
        }
        return true;

    }
}
