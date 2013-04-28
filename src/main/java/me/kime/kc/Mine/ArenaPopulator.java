package me.kime.kc.Mine;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Sign;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 *
 * @author Kime
 */
public class ArenaPopulator extends BlockPopulator {

    private static final int GENERATE_CHANCE = 2;

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (random.nextInt(1000) <= GENERATE_CHANCE) {
            int centerX = (chunk.getX() << 4) + random.nextInt(16);
            int centerZ = (chunk.getZ() << 4) + random.nextInt(16);
            int centerY = world.getHighestBlockYAt(centerX, centerZ) + 30;
            createPlatform(world, random, centerX, centerY, centerZ);
        }
    }

    private void createPlatform(World world, Random random, int centerX, int centerY, int centerZ) {
        int type = random.nextInt(2);
        switch (type) {
            case 0:
                platform0(world, random, centerX, centerY, centerZ);
                break;
            case 1:
                platform1(world, random, centerX, centerY, centerZ);
                break;
            default:
                platform0(world, random, centerX, centerY, centerZ);
                break;
        }
    }

    private void platform0(World world, Random random, int centerX, int centerY, int centerZ) {
        Vector position;
        Vector center = new BlockVector(centerX, centerY, centerZ);
        position = center.clone().add(new Vector(0, 1, 0));
        Block block = world.getBlockAt(position.toLocation(world));
        block.setType(Material.CHEST);
        Inventory inventory = ((Chest) block.getState()).getBlockInventory();
        for (int i = 0; i < 5; i++) {
            inventory.addItem(new ItemStack(Material.STONE_SPADE));
        }
        int size = random.nextInt(30) + 10;
        for (int x = 0; x <= size; x++) {
            for (int z = 0; z <= size; z++) {
                position = center.clone().add(new Vector(x, 0, z));
                if (x == 0 || x == size || z == 0 || z == size) {
                    world.getBlockAt(position.toLocation(world)).setType(Material.BEDROCK);
                } else {
                    world.getBlockAt(position.toLocation(world)).setType(Material.SNOW_BLOCK);
                }
            }
        }
    }

    private void platform1(World world, Random random, int centerX, int centerY, int centerZ) {
        Vector center = new BlockVector(centerX, centerY, centerZ);
        int size = random.nextInt(20) + 10;
        for (int x = 0; x <= size; x++) {
            for (int z = 0; z <= size; z++) {
                Vector position = center.clone().add(new Vector(x, 0, z));
                if (x == 1 && z == 1) {
                    position = center.clone().add(new Vector(x, -1, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.NETHERRACK);
                    position = center.clone().add(new Vector(x, 0, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.REDSTONE_WIRE);
                    position = center.clone().add(new Vector(x, 1, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.SANDSTONE);
                    position = center.clone().add(new Vector(x, 2, z));
                    world.getBlockAt(position.toLocation(world)).setTypeIdAndData(Material.LEVER.getId(), (byte) 0x5, false);
                } else if (x == 0 || z == 0) {
                    if (x == 0 && z == 0) {
                        continue;
                    }
                    position = center.clone().add(new Vector(x, -1, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.NETHERRACK);
                    position = center.clone().add(new Vector(x, 0, z));
                    if (((x % 11) == 0 && z == 0) || ((z % 11) == 0 && x == 0)) {
                        if (x == 0) {
                            world.getBlockAt(position.toLocation(world)).setTypeIdAndData(Material.DIODE_BLOCK_OFF.getId(), (byte) 0x2, false);
                        } else {
                            world.getBlockAt(position.toLocation(world)).setTypeIdAndData(Material.DIODE_BLOCK_OFF.getId(), (byte) 0x1, false);
                        }

                    } else {
                        world.getBlockAt(position.toLocation(world)).setType(Material.REDSTONE_WIRE);
                    }
                } else if (x == 1 || x == size || z == 1 || z == size) {
                    world.getBlockAt(position.toLocation(world)).setType(Material.NETHERRACK);
                    position = center.clone().add(new Vector(x, 1, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.FIRE);
                    if (random.nextInt(3) == 1 && (x == 1 || z == 1)) {
                        position = center.clone().add(new Vector(x, -1, z));
                        Block block = world.getBlockAt(position.toLocation(world));
                        if (block.getType() == Material.WALL_SIGN) {
                            continue;
                        }
                        block.setType(Material.DISPENSER);
                        Block block2;
                        if (x == 1) {
                            ((org.bukkit.material.Dispenser) block.getState().getData()).setFacingDirection(BlockFace.SOUTH);
                            position = center.clone().add(new Vector(x, -1, z + 1));
                            block2 = world.getBlockAt(position.toLocation(world));
                        } else {
                            ((org.bukkit.material.Dispenser) block.getState().getData()).setFacingDirection(BlockFace.EAST);
                            position = center.clone().add(new Vector(x + 1, -1, z));
                            block2 = world.getBlockAt(position.toLocation(world));
                        }
                        Inventory inventory = ((Dispenser) block.getState()).getInventory();
                        inventory.addItem(new ItemStack(Material.FIREBALL, 5));
                        if (x == 1) {
                            block2.setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 0x03, false);
                        } else {
                            block2.setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 0x05, false);
                        }
                        Sign sign = (Sign) block2.getState();
                        sign.setLine(0, "[Private]");
                        sign.setLine(1, "KIME-SERVER");
                        sign.update();
                    }
                } else {
                    world.getBlockAt(position.toLocation(world)).setType(Material.LOG);
                    position = center.clone().add(new Vector(x, 1, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.SAND);
                }
            }
        }
    }
}
