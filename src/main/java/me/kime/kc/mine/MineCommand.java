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
package me.kime.kc.mine;

import java.text.DecimalFormat;

import me.kime.kc.KPlayer;
import me.kime.kc.util.KMessager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class MineCommand implements CommandExecutor {

    private final Mine mine;
    private final DecimalFormat format;

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
        KPlayer kPlayer = mine.getPlugin().getOnlinePlayer(player.getName());
        if (split.length == 0) {
            if (player.getWorld().equals(mine.getPlugin().getDefaultWorld())) {
                player.teleport(mine.getMineWorld().getSpawnLocation(), TeleportCause.COMMAND);
                KMessager.sendMessage(kPlayer, ChatColor.GREEN, "mine_welcome");
            } else {
                KMessager.sendError(kPlayer, "mine_disallowEnter");
            }
            return true;
        } else if (split.length == 1) {
            if ("salary".equalsIgnoreCase(split[0])) {
                KMessager.sendMessage(kPlayer, ChatColor.BLUE, "mine_salaryEarnedTotal", format.format(kPlayer.getTotalSalary()));
                return true;
            }

        }

        KMessager.sendError(kPlayer, "mine_salaryUsage");
        return true;
    }
}
