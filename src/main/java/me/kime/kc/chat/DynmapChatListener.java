/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
