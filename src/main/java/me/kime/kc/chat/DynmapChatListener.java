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
import me.kime.kc.util.KLogger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.dynmap.DynmapWebChatEvent;

/**
 *
 * @author Kime
 */
public class DynmapChatListener implements Listener {
    
    private final Chat addon;
    
    public DynmapChatListener(Chat addon) {
        this.addon = addon;
    }
    
    @EventHandler
    public void onDynmapWebChatMessage(DynmapWebChatEvent event) {
        if (!addon.getPlugin().dynampIntegration || event.isCancelled()) {
            return;
        }        
        KPlayer kPlayer = addon.getPlugin().getOnlinePlayer(event.getName());
        if (kPlayer == null || kPlayer.isAuth()) {
            event.setProcessed();
            addon.webChat(event.getName(), event.getMessage());
        } else {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        KPlayer kPlayer = addon.getPlugin().getOnlinePlayer(event.getPlayer().getName());
        if (!kPlayer.isAuth()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap del-id-for-ip " + kPlayer.player.getName() + " " + kPlayer.player.getAddress().getHostString());
            KLogger.info("Remove " + kPlayer.player.getName() + " from web chat list");
        }        
    }
}
