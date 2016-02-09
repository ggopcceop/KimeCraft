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
package me.kime.kc.task;

import me.kime.kc.KPlayer;
import me.kime.kc.locale.Locale;
import me.kime.kc.addon.motd.MOTD;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Kime
 */
public class MOTDBoradcastTask implements Runnable {

    private final MOTD addon;
    private int line;
    private final int maxline;

    public MOTDBoradcastTask(MOTD instance) {
        addon = instance;
        line = 0;
        maxline = addon.rules.get("en_US").size();
    }

    @Override
    public void run() {
        line = addon.getPlugin().getRandom().nextInt(maxline);
        for (Player player : Bukkit.getOnlinePlayers()) {
            KPlayer kPlayer = addon.getPlugin().getOnlinePlayer(player.getName());
            String language;
            if (kPlayer.getLocale() == null) {
                language = "en_US";
            } else {
                language = kPlayer.getLocale().getLanguage();
            }
            Locale locale = addon.rules.get(language);
            if (locale == null) {
                locale = addon.rules.get("en_US");
            }
            String rule = locale.phrase(line + "");
            player.sendMessage(ChatColor.BLUE + rule);
        }
    }

}
