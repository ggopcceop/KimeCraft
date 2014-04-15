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
package me.kime.kc.chopTree;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class ChopTreeBlockListener
        implements Listener {

    public static Player pubplayer = null;
    private final ChopTree chopTree;

    public ChopTreeBlockListener(ChopTree chopTree) {
        this.chopTree = chopTree;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.LOG && block.getData() != 3) {
            if (denyPermission(event.getPlayer())) {
                return;
            }
            if (denyItem(event.getPlayer())) {
                return;
            }

            event.setCancelled(true);

            if (Chop(event.getBlock(), event.getPlayer(), event.getBlock().getWorld())) {
                if ((breaksTool(event.getPlayer(), event.getPlayer().getItemInHand()))) {
                    event.getPlayer().getInventory().clear(event.getPlayer().getInventory().getHeldItemSlot());
                }
            } else {
                event.setCancelled(false);
            }
        }
    }

    private boolean Chop(Block block, Player player, World world) {
        List<Block> blocks = new LinkedList<>();
        Block highest = getHighestLog(block);
        if (isTree(highest, player, block)) {
            getBlocksToChop(block, highest, blocks);

            popLogs(block, blocks, world, player);

            blocks.clear();
        } else {
            return false;
        }
        return true;
    }

    private void getBlocksToChop(Block block, Block highest, List<Block> blocks) {
        while (block.getY() <= highest.getY()) {
            if (!blocks.contains(block)) {
                blocks.add(block);
            }
            getBranches(block, blocks, block.getRelative(BlockFace.NORTH));
            getBranches(block, blocks, block.getRelative(BlockFace.NORTH_EAST));
            getBranches(block, blocks, block.getRelative(BlockFace.EAST));
            getBranches(block, blocks, block.getRelative(BlockFace.SOUTH_EAST));
            getBranches(block, blocks, block.getRelative(BlockFace.SOUTH));
            getBranches(block, blocks, block.getRelative(BlockFace.SOUTH_WEST));
            getBranches(block, blocks, block.getRelative(BlockFace.WEST));
            getBranches(block, blocks, block.getRelative(BlockFace.NORTH_WEST));
            if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH))) {
                getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH));
            }
            if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST))) {
                getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST));
            }
            if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST))) {
                getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST));
            }
            if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST))) {
                getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST));
            }
            if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH))) {
                getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH));
            }
            if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST))) {
                getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST));
            }
            if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST))) {
                getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST));
            }
            if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST))) {
                getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST));
            }

            if ((block.getData() == 3) || (block.getData() == 7) || (block.getData() == 11) || (block.getData() == 15)) {
                if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH, 2))) {
                    getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH, 2));
                }
                if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST, 2))) {
                    getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST, 2));
                }
                if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST, 2))) {
                    getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST, 2));
                }
                if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST, 2))) {
                    getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST, 2));
                }
                if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH, 2))) {
                    getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH, 2));
                }
                if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST, 2))) {
                    getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST, 2));
                }
                if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST, 2))) {
                    getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST, 2));
                }
                if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST, 2))) {
                    getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST, 2));
                }
            }
            if ((blocks.contains(block.getRelative(BlockFace.UP))) || (block.getRelative(BlockFace.UP).getType() != Material.LOG)) {
                break;
            }
            block = block.getRelative(BlockFace.UP);
        }
    }

    private void getBranches(Block block, List<Block> blocks, Block other) {
        if ((!blocks.contains(other)) && (other.getType() == Material.LOG && other.getData() != 3)) {
            getBlocksToChop(other, getHighestLog(other), blocks);
        }
    }

    private Block getHighestLog(Block block) {
        while (block.getRelative(BlockFace.UP).getType() == Material.LOG && block.getRelative(BlockFace.UP).getData() != 3) {
            block = block.getRelative(BlockFace.UP);
        }
        return block;
    }

    private boolean isTree(Block block, Player player, Block first) {
        int counter = 0;
        if (block.getRelative(BlockFace.UP).getType() == Material.LEAVES) {
            counter++;
        }
        if (block.getRelative(BlockFace.DOWN).getType() == Material.LEAVES) {
            counter++;
        }
        if (block.getRelative(BlockFace.NORTH).getType() == Material.LEAVES) {
            counter++;
        }
        if (block.getRelative(BlockFace.EAST).getType() == Material.LEAVES) {
            counter++;
        }
        if (block.getRelative(BlockFace.SOUTH).getType() == Material.LEAVES) {
            counter++;
        }
        if (block.getRelative(BlockFace.WEST).getType() == Material.LEAVES) {
            counter++;
        }
        if (counter >= 2) {
            return true;
        }
        if (block.getData() == 1) {
            block = block.getRelative(BlockFace.UP);
            if (block.getRelative(BlockFace.UP).getType() == Material.LEAVES) {
                counter++;
            }
            if (block.getRelative(BlockFace.NORTH).getType() == Material.LEAVES) {
                counter++;
            }
            if (block.getRelative(BlockFace.EAST).getType() == Material.LEAVES) {
                counter++;
            }
            if (block.getRelative(BlockFace.SOUTH).getType() == Material.LEAVES) {
                counter++;
            }
            if (block.getRelative(BlockFace.WEST).getType() == Material.LEAVES) {
                counter++;
            }
            if (counter >= 2) {
                return true;
            }
        }
        return false;
    }

    private void popLogs(Block block, List<Block> blocks, World world, Player player) {
        ItemStack item = new ItemStack(1, 1, (short) 0);
        item.setAmount(1);
        for (Block b : blocks) {
            block = (Block) b;
            item.setType(block.getType());
            item.setDurability((short) block.getData());
            block.breakNaturally();
            if (chopTree.isPopLeaves()) {
                popLeaves(block);
            }
            if ((!chopTree.isMoreDamageToTools())
                    || (!breaksTool(player, player.getItemInHand()))) {
                continue;
            }
            player.getInventory().clear(player.getInventory().getHeldItemSlot());
            if (chopTree.isInterruptIfToolBreaks()) {
                break;
            }
        }
    }

    private void popLeaves(Block block) {
        for (int y = -chopTree.getLeafRadius(); y < chopTree.getLeafRadius() + 1; y++) {
            for (int x = -chopTree.getLeafRadius(); x < chopTree.getLeafRadius() + 1; x++) {
                for (int z = -chopTree.getLeafRadius(); z < chopTree.getLeafRadius() + 1; z++) {
                    Block target = block.getRelative(x, y, z);
                    if (target.getType().equals(Material.LEAVES)) {
                        target.breakNaturally();
                    }
                }
            }
        }
    }

    private boolean breaksTool(Player player, ItemStack item) {
        if ((item != null)
                && (isTool(item.getTypeId()))) {
            short damage = item.getDurability();
            if (isAxe(item.getTypeId())) {
                damage = (short) (damage + 1);
            } else {
                damage = (short) (damage + 2);
            }
            if (damage >= item.getType().getMaxDurability()) {
                return true;
            }
            item.setDurability(damage);
        }

        return false;
    }

    private boolean isTool(int ID) {
        return (ID == 256) || (ID == 257) || (ID == 258) || (ID == 267) || (ID == 268) || (ID == 269) || (ID == 270) || (ID == 271) || (ID == 272) || (ID == 273) || (ID == 274) || (ID == 275) || (ID == 276) || (ID == 277) || (ID == 278) || (ID == 279) || (ID == 283) || (ID == 284) || (ID == 285) || (ID == 286);
    }

    private boolean isAxe(int ID) {
        return (ID == 258) || (ID == 271) || (ID == 275) || (ID == 278) || (ID == 286);
    }

    private boolean isLoneLog(Block block) {
        if (block.getRelative(BlockFace.UP).getType() == Material.LOG) {
            return false;
        }
        if (block.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            return false;
        }
        if (hasHorizontalCompany(block)) {
            return false;
        }
        if (hasHorizontalCompany(block.getRelative(BlockFace.UP))) {
            return false;
        }
        return !hasHorizontalCompany(block.getRelative(BlockFace.DOWN));
    }

    private boolean hasHorizontalCompany(Block block) {
        if (block.getRelative(BlockFace.NORTH).getType() == Material.LOG) {
            return true;
        }
        if (block.getRelative(BlockFace.NORTH_EAST).getType() == Material.LOG) {
            return true;
        }
        if (block.getRelative(BlockFace.EAST).getType() == Material.LOG) {
            return true;
        }
        if (block.getRelative(BlockFace.SOUTH_EAST).getType() == Material.LOG) {
            return true;
        }
        if (block.getRelative(BlockFace.SOUTH).getType() == Material.LOG) {
            return true;
        }
        if (block.getRelative(BlockFace.SOUTH_WEST).getType() == Material.LOG) {
            return true;
        }
        if (block.getRelative(BlockFace.WEST).getType() == Material.LOG) {
            return true;
        }
        return block.getRelative(BlockFace.NORTH_WEST).getType() == Material.LOG;
    }

    private boolean denyPermission(Player player) {
        return !player.hasPermission("choptree.chop");
    }

    private boolean denyItem(Player player) {
        ItemStack item = player.getItemInHand();
        if (item != null) {
            for (String tool : chopTree.getAllowedTools()) {
                if (tool.equalsIgnoreCase(item.getType().name())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean interruptWhenBreak(Player player) {
        return chopTree.isInterruptIfToolBreaks();
    }
}
