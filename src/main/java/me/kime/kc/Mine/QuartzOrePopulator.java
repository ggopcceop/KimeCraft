/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class QuartzOrePopulator extends BlockPopulator {

    private static final int BIG_ORE_CHANCE = 15;
    private static final int ORE_CHANCE = 75;

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (int i = 0; i < 6; i++) {
            int centerX = (chunk.getX() << 4) + random.nextInt(16);
            int centerZ = (chunk.getZ() << 4) + random.nextInt(16);
            int centerY = random.nextInt(world.getHighestBlockYAt(centerX, centerZ) - 16) + 8;
            createOre(world, random, centerX, centerY, centerZ);
        }
    }

    private void createOre(World world, Random random, int centerX, int centerY, int centerZ) {
        Vector center = new BlockVector(centerX, centerY, centerZ);
        int hight = random.nextInt(100) <= BIG_ORE_CHANCE ? 2 : 1;
        int width = random.nextInt(100) <= BIG_ORE_CHANCE ? 2 : 1;
        int legth = random.nextInt(100) <= BIG_ORE_CHANCE ? 2 : 1;

        for (int x = 0; x <= hight; x++) {
            for (int y = 0; y <= width; y++) {
                for (int z = 0; z <= legth; z++) {
                    if (random.nextInt(100) <= ORE_CHANCE) {
                        Vector position = center.clone().add(new Vector(x, y, z));
                        if (world.getBlockAt(position.toLocation(world)).getTypeId() == 1) {
                            world.getBlockAt(position.toLocation(world)).setType(Material.QUARTZ_ORE);
                        }
                    }
                }
            }
        }
    }
}
