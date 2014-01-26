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
package me.kime.kc.util;

import me.kime.kc.KPlayer;
import me.kime.kc.KimeCraft;
import me.kime.kc.locale.Locale;
import me.kime.kc.locale.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * plugin messager to send player message
 *
 * @author Kime
 *
 */
public class KMessager {

    private static KimeCraft plugin = null;

    public static void sendError(KPlayer player, String key, String... words) {
        if (player != null) {
            sendMessage(player, ChatColor.RED, key, words);
        }
    }

    public static void sendMessage(KPlayer player, ChatColor color, String key, String... words) {
        if (player != null) {
            Locale locale = player.getLocale();
            if (locale == null) {
                locale = LocaleManager.getDefauLocale();
            }
            player.player.sendMessage(color + locale.phrase(key, words));
        }
    }

    public static void boardcastMessage(ChatColor color, String key, String... words) {
        Player[] players = Bukkit.getOnlinePlayers();
        if (plugin == null) {
            plugin = (KimeCraft) Bukkit.getPluginManager().getPlugin("KimeCraft");
        }
        for (Player player : players) {
            KPlayer kPlayer = plugin.getOnlinePlayer(player.getName());
            sendMessage(kPlayer, color, key, words);
        }

    }
}
