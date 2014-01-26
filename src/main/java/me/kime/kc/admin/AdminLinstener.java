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
package me.kime.kc.admin;

import me.kime.kc.util.KCItemManger;
import me.kime.kc.util.KMessager;
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
                    KMessager.sendError(admin.getPlugin().getOnlinePlayer(player.getName()), "admin_signFormatIncorrect");
                    event.setCancelled(true);
                }
            }
        }
    }
}
