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
package me.kime.kc.motd;

import me.kime.kc.KPlayer;
import me.kime.kc.auth.AuthSucceedEvent;
import me.kime.kc.locale.Locale;
import me.kime.kc.locale.LocaleManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author Kime
 */
public class MOTDListener implements Listener {

    private final MOTD addon;

    MOTDListener(MOTD addon) {
        this.addon = addon;
    }

    @EventHandler
    public void onAuthSucceed(AuthSucceedEvent event) {
        KPlayer player = event.getPlayer();

        String language;
        if (player.getLocale() == null) {
            language = LocaleManager.getDefauLocale().getLanguage();
        } else {
            language = player.getLocale().getLanguage();
        }

        Locale locale = addon.motd.get(language);
        if (locale == null) {
            locale = addon.motd.get("en_US");
        }
        player.player.sendMessage(ChatColor.LIGHT_PURPLE + locale.phrase("motd", player.player.getName()));
    }
}
