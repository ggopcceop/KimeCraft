package me.kime.kc.Admin;

import me.kime.kc.Util.KCItemManger;
import me.kime.kc.Util.KCMessager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

/**
 *
 * @author Kime
 */
public class AdminLinstener implements Listener {

    private final Admin admin;

    AdminLinstener(Admin admin) {
        this.admin = admin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();
        if ((clickedBlock.getState() instanceof Sign)) {
            if (player.hasPermission("kc.admin.itemchest") && !player.isSneaking()) {
                Sign sign = (Sign) clickedBlock.getState();
                String[] lines = sign.getLines();
                try {
                    if (lines[0].toLowerCase().startsWith("itemchest")) {
                        Block chestBlock = clickedBlock.getRelative(BlockFace.DOWN);
                        if (chestBlock.getState() instanceof Chest) {
                            Inventory chest = ((Chest) chestBlock.getState()).getInventory();
                            String[] split;
                            if (lines[1].length() > 0) {
                                split = lines[1].split(" ");
                                KCItemManger.addItemToInventory(split, chest);
                            }
                            if (lines[2].length() > 0) {
                                split = lines[2].split(" ");
                                KCItemManger.addItemToInventory(split, chest);
                            }
                            if (lines[3].length() > 0) {
                                split = lines[3].split(" ");
                                KCItemManger.addItemToInventory(split, chest);
                            }
                            event.setCancelled(true);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    KCMessager.sentMessage(player, "Sign format incorrect", ChatColor.RED);
                    event.setCancelled(true);
                }
            }
        }
    }
}
