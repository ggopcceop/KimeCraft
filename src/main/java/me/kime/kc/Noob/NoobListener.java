package me.kime.kc.Noob;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class NoobListener implements Listener {

    private Noob noob;

    public NoobListener(Noob noob) {
        this.noob = noob;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getLocation().getWorld() == noob.getNoobWorld()) {
            switch (event.getItemInHand().getTypeId()) {
                case 331:
                case 130:
                case 76:
                case 27:
                case 28:
                case 29:
                case 33:
                case 66:
                case 23:
                    event.setCancelled(true);
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        if (event.getVehicle().getLocation().getWorld() == noob.getNoobWorld()) {
            event.getVehicle().remove();
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getLocation().getWorld() == noob.getNoobWorld()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExpBottle(ExpBottleEvent event) {
        if (event.getEntity().getLocation().getWorld() == noob.getNoobWorld()) {
            event.setExperience(0);
            event.setShowEffect(false);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent event) {
        if (event.getEntity().getLocation().getWorld() == noob.getNoobWorld()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        World currentWorld = event.getFrom().getWorld();
        World toWorld = event.getTo().getWorld();
        World noobWorld = noob.getNoobWorld();
        Player player = event.getPlayer();

        if ((toWorld == noobWorld) && (currentWorld != noobWorld)) {
            if (!event.getPlayer().hasPermission("kc.admin.noobitem")) {
                player.setGameMode(GameMode.CREATIVE);
            }

        } else if ((toWorld != noobWorld) && (currentWorld == noobWorld)) {
            if (!event.getPlayer().hasPermission("kc.admin.noobitem")) {
                player.setGameMode(GameMode.SURVIVAL);
                player.getInventory().clear();
                player.getEnderChest().clear();
                player.setExp(0);
                player.setLevel(0);

                PlayerInventory inv = player.getInventory();
                inv.setHelmet(new ItemStack(298));
                inv.setChestplate(new ItemStack(299));
                inv.setLeggings(new ItemStack(300));
                inv.setBoots(new ItemStack(301));
                inv.addItem(new ItemStack(270));
                inv.addItem(new ItemStack(271));
                inv.addItem(new ItemStack(268));
                inv.addItem(new ItemStack(297, 10));
                inv.addItem(new ItemStack(17, 10));
                inv.addItem(new ItemStack(50, 20));
            }
        }
    }
}
