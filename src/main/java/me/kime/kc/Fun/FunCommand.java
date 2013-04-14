package me.kime.kc.Fun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class FunCommand implements CommandExecutor {

    public FunCommand(Fun fun) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (player.isOp() && (split.length == 1)) {
            ItemStack item = player.getItemInHand();
            if (item.getTypeId() == 397) {
                SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
                skullMeta.setOwner(split[0]);
                item.setItemMeta(skullMeta);
            }
        }
        return true;
    }
}
