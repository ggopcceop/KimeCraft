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
 *//*
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
package me.kime.kc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import me.kime.kc.locale.Locale;
import me.kime.kc.locale.LocaleManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Kime
 */
public class KCListener implements Listener {

    private final KimeCraft plugin;
    private final HashMap<String, KPlayer> onlineList;

    KCListener(KimeCraft instance, HashMap<String, KPlayer> onlineList) {
        this.plugin = instance;
        this.onlineList = onlineList;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        KPlayer kPlayer = new KPlayer(player);
        onlineList.put(player.getName().toLowerCase(), kPlayer);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        onlineList.remove(event.getPlayer().getName().toLowerCase());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        KPlayer kPlayer = plugin.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer != null) {
            if (kPlayer.getLocale() == null) {
                Locale locale = LocaleManager.getLocale(getLanguage(event.getPlayer()));
                kPlayer.setLocale(locale);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        KPlayer kPlayer = plugin.getOnlinePlayer(event.getPlayer().getName());
        if (kPlayer != null) {
            if (kPlayer.getLocale() == null) {
                Locale locale = LocaleManager.getLocale(getLanguage(event.getPlayer()));
                kPlayer.setLocale(locale);
            }
        }
    }

    private String getLanguage(Player p) {
        try {
            Object ep = getMethod("getHandle", p.getClass()).invoke(p, (Object[]) null);
            Field f = ep.getClass().getDeclaredField("locale");
            f.setAccessible(true);
            String language = (String) f.get(ep);
            return language;
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | InvocationTargetException ex) {
            return "en_US";
        }
    }

    private Method getMethod(String name, Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }
}
