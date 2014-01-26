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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 *
 * @author Kime
 */
public class ChatListener implements Listener {

    private final Chat addon;
    private int normalChatRange;

    public ChatListener(Chat addon) {
        this.addon = addon;

        normalChatRange = addon.getPlugin().config.getInt("chat.normalChatRange", 40);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        KPlayer kPlayer = addon.getPlugin().getOnlinePlayer(event.getPlayer().getName());
        switch (kPlayer.currentChannel) {
            case 0:
                String message = "[" + kPlayer.player.getName() + "]: " + event.getMessage();
                addon.rangeChat(event.getPlayer(), normalChatRange, message);
                event.setCancelled(true);
                break;
            case 1:
                addon.publicChat(event.getPlayer(), event.getMessage());
                event.setCancelled(true);
                break;
            default:
                break;
        }
    }
}
