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

import me.kime.kc.Addon;
import me.kime.kc.KPlayer;
import me.kime.kc.KimeCraft;
import me.kime.kc.locale.Locale;
import me.kime.kc.locale.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author Kime
 */
public class Chat extends Addon {

    public Chat(KimeCraft plugin) {
        super(plugin);
    }

    @Override
    public String getAddonName() {
        return "Chat";
    }

    @Override
    public void onEnable() {
        ChatCommand command = new ChatCommand(this);
        plugin.getCommand("chat").setExecutor(command);

        plugin.getPluginManager().registerEvents(new ChatListener(this), plugin);
    }

    @Override
    public void onDisable() {

    }

    public void rangeChat(Entity sender, int range, String message) {
        World currentWorld = sender.getWorld();
        Location centerLocation = sender.getLocation();
        Player[] players = Bukkit.getOnlinePlayers();
        int squaredRange = range * range;
        for (Player player : players) {
            if (player.getWorld() == currentWorld) {
                if (centerLocation.distanceSquared(player.getLocation()) <= squaredRange) {
                    player.sendMessage(message);
                }
            }
        }
    }

    public void publicChat(Entity sender, String message) {
        Player[] players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            StringBuilder result = new StringBuilder();
            KPlayer kPlayer = plugin.getOnlinePlayer(player.getName());
            Locale locale = kPlayer.getLocale();

            if (locale == null) {
                locale = LocaleManager.getDefauLocale();
            }

            result.append(ChatColor.GOLD.toString());
            result.append(locale.phrase("chat_public"));
            result.append("[");
            result.append(player.getName());
            result.append("]: ");
            result.append(message);

            player.sendMessage(result.toString());
        }
    }

    public void channelChat(Entity sender, String channel, String message) {

    }
}
