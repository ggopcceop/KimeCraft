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
package me.kime.kc.chat;

import me.kime.kc.KPlayer;
import me.kime.kc.util.KMessager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Kime
 */
public class ChatCommand implements CommandExecutor {

    private final Chat addon;

    public ChatCommand(Chat addon) {
        this.addon = addon;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        KPlayer kPlayer = addon.getPlugin().getOnlinePlayer(player.getName());
        if (split.length == 0) {
            switch (kPlayer.currentChannel) {
                case 0:
                    KMessager.sendMessage(kPlayer, ChatColor.DARK_GREEN, "chat_chattingChannel", kPlayer.getLocale().phrase("chat_normal"));
                    break;
                case 1:
                    KMessager.sendMessage(kPlayer, ChatColor.DARK_GREEN, "chat_chattingChannel", kPlayer.getLocale().phrase("chat_public"));
                    break;
            }
        } else {
            switch (split[0].toLowerCase()) {
                case "0":
                case "normal":
                    if (split.length == 1) {
                        kPlayer.currentChannel = 0;
                        KMessager.sendMessage(kPlayer, ChatColor.DARK_GREEN, "chat_chattingChannel", kPlayer.getLocale().phrase("chat_normal"));
                    } else {
                        addon.rangeChat(player.getName(), player.getLocation(), addon.getNormalChatRange(), split[1]);
                    }
                    break;
                case "1":
                case "public":
                    if (split.length == 1) {
                        kPlayer.currentChannel = 1;
                        KMessager.sendMessage(kPlayer, ChatColor.DARK_GREEN, "chat_chattingChannel", kPlayer.getLocale().phrase("chat_public"));
                    } else {
                        addon.publicChat(player, split[1]);
                    }
                    break;
                default:
                    break;
            }

            return true;
        }
        return true;
    }

}
