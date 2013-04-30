package me.kime.kc.Mine;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 *
 * @author Kime
 */
public class LobbyPopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (world.getSpawnLocation().getChunk().equals(chunk)) {
            int centerX = world.getSpawnLocation().getBlockX();
            int centerZ = world.getSpawnLocation().getBlockZ();
            int centerY = world.getHighestBlockYAt(centerX, centerZ);
            world.setSpawnLocation(centerX, centerY, centerZ);

            createPlatform(world, random, centerX, centerY - 2, centerZ);
            createRoof(world, random, centerX, centerY + 5, centerZ);
            createPortal(world, random, centerX, centerY - 1, centerZ - 5);
        }
    }

    private void createPortal(World world, Random random, int centerX, int centerY, int centerZ) {
        Vector center = new BlockVector(centerX, centerY, centerZ);
        int w = 2, h = 5;
        for (int x = -w; x <= w; x++) {
            for (int y = 0; y <= h; y++) {
                if (Math.abs(x) == w || (y == 0 || y == h)) {
                    Vector position = center.clone().add(new Vector(x, y, 0));
                    world.getBlockAt(position.toLocation(world)).setTypeId(Material.MOSSY_COBBLESTONE.getId(), false);
                } else {
                    Vector position = center.clone().add(new Vector(x, y, 0));
                    world.getBlockAt(position.toLocation(world)).setTypeId(Material.PORTAL.getId(), false);
                }
            }
        }
    }

    private void createPlatform(World world, Random random, int centerX, int centerY, int centerZ) {
        Vector center = new BlockVector(centerX, centerY, centerZ);
        int r = 7;
        int rr = r * r;
        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                if ((x * x) + (z * z) <= rr) {
                    Vector position = center.clone().add(new Vector(x, 0, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.QUARTZ_BLOCK);
                    for (int y = 1; y <= 6; y++) {
                        position = center.clone().add(new Vector(x, y, z));
                        world.getBlockAt(position.toLocation(world)).setType(Material.AIR);
                    }
                }
            }
        }
    }

    private void createRoof(World world, Random random, int centerX, int centerY, int centerZ) {
        Vector center = new BlockVector(centerX, centerY, centerZ);
        int r1 = 7;
        int r2 = 6;
        int rr1 = r1 * r1;
        int rr2 = r2 * r2;
        for (int x = -r1; x <= r1; x++) {
            for (int z = -r1; z <= r1; z++) {
                int tmp = (x * x) + (z * z);
                if (tmp <= rr2) {
                    Vector position = center.clone().add(new Vector(x, 0, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.ENDER_PORTAL);
                    position = center.clone().add(new Vector(x, 1, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.LAVA);
                } else if (tmp <= rr1) {
                    Vector position = center.clone().add(new Vector(x, 0, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.BEDROCK);
                    position = center.clone().add(new Vector(x, 1, z));
                    world.getBlockAt(position.toLocation(world)).setType(Material.BEDROCK);
                }
            }
        }
    }
}
