package me.kime.kc.Portal;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 *
 * @author Kime
 */
public class PortalLinstener implements Listener {
    
    private final Portal portal;
    
    PortalLinstener(Portal portal) {
        this.portal = portal;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block block = event.getBlock().getRelative(BlockFace.DOWN);
        if (PortalManager.isPortalBase(block.getTypeId())) {
            if (PortalManager.allowedWorld(block.getWorld().getName())) {
                if (PortalManager.isPortal(block, block.getTypeId())) {
                    PortalManager.createPortalByFrame(block, block.getTypeId());
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (event.getCause() == TeleportCause.NETHER_PORTAL) {
            Location from = event.getFrom();
            if (PortalManager.allowedWorld(from.getWorld().getName())) {
                Block block = PortalManager.findBaseBlock(from.getBlock());
                if (PortalManager.isPortalBase(block.getTypeId())) {
                    Location to = event.getTo();
                    if (to == null) {
                        to = from.clone();
                    }
                    if (PortalManager.setTo(block.getTypeId(), from, to, event.getPortalTravelAgent(), portal)) {
                        event.setTo(to);
                    } else {
                        event.setCancelled(true);
                    }
                    
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityPortal(EntityPortalEvent event) {
        Location from = event.getFrom();
        if (PortalManager.allowedWorld(from.getWorld().getName())) {
            Block block = PortalManager.findBaseBlock(from.getBlock());
            if (PortalManager.isPortalBase(block.getTypeId())) {
                Location to = event.getTo();
                if (to == null) {
                    to = from.clone();
                }
                if (PortalManager.setTo(block.getTypeId(), from, to, event.getPortalTravelAgent(), portal)) {
                    event.setTo(to);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getTypeId() == 45) {
            if (event.getBlock().getWorld() == portal.getPlugin().getNether()) {
                event.getBlock().setTypeId(0);
            }
        }
    }
}
