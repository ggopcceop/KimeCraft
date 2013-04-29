package me.kime.kc.Portal;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
                    Player player = event.getPlayer();
                    Location to = from.clone();
                    switch (block.getTypeId()) {
                        case 45:
                            player.teleport(portal.getPlugin().getMine().getMineWorld().getSpawnLocation());
                            event.setCancelled(true);
                            break;
                        case 48:
                            to.setWorld(portal.getPlugin().getDefaultWorld());
                            player.teleport(to);
                            event.setCancelled(true);
                            break;
                        case 49:
                            to.setWorld(portal.getPlugin().getNether());
                            player.teleport(to);
                            player.setNoDamageTicks(60);
                            event.setCancelled(true);
                            break;
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
                Entity entity = event.getEntity();
                Location to = from.clone();
                switch (block.getTypeId()) {
                    case 45:
                        entity.teleport(portal.getPlugin().getMine().getMineWorld().getSpawnLocation());
                        event.setCancelled(true);
                        break;
                    case 48:
                        if (from.getWorld() == portal.getPlugin().getMine().getMineWorld()) {
                            to = portal.getPlugin().getCity();
                        } else {
                            to.setWorld(portal.getPlugin().getDefaultWorld());
                        }
                        entity.teleport(to);
                        event.setCancelled(true);
                        break;
                    case 49:
                        to.setWorld(portal.getPlugin().getNether());
                        entity.teleport(to);
                        event.setCancelled(true);
                        break;
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
