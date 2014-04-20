/*
 * Copyright (C) 2014 Kime
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.kime.kc.addon.fun;

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
