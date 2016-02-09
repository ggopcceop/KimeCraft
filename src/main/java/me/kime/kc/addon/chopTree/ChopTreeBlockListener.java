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
package me.kime.kc.addon.chopTree;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
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
    private final Random random;

    public ChopTreeBlockListener(ChopTree chopTree) {
        this.chopTree = chopTree;

        random = new Random();
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
                && (isTool(item))) {
            short durability = item.getDurability();
            short damage = 0;
            int level = item.getEnchantmentLevel(Enchantment.DURABILITY);
            if (random.nextInt(100) <= (100.0 / (level + 1))) {
                if (isAxe(item)) {
                    damage = 1;
                } else {
                    damage = 2;
                }
            }

            durability += damage;
            if (durability >= item.getType().getMaxDurability()) {
                return true;
            }
            item.setDurability(durability);
        }

        return false;
    }

    private boolean isTool(ItemStack item) {
        switch (item.getType()) {
            case WOOD_AXE:
            case WOOD_HOE:
            case WOOD_PICKAXE:
            case WOOD_SPADE:

            case STONE_AXE:
            case STONE_HOE:
            case STONE_PICKAXE:
            case STONE_SPADE:

            case IRON_AXE:
            case IRON_HOE:
            case IRON_PICKAXE:
            case IRON_SPADE:

            case GOLD_AXE:
            case GOLD_HOE:
            case GOLD_PICKAXE:
            case GOLD_SPADE:

            case DIAMOND_AXE:
            case DIAMOND_HOE:
            case DIAMOND_PICKAXE:
            case DIAMOND_SPADE:
                return true;
            default:
                return false;

        }
    }

    private boolean isAxe(ItemStack item) {
        switch (item.getType()) {
            case WOOD_AXE:
            case STONE_AXE:
            case IRON_AXE:
            case GOLD_AXE:
            case DIAMOND_AXE:
                return true;
            default:
                return false;

        }
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
