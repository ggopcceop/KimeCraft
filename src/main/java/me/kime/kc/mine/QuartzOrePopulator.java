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
package me.kime.kc.mine;

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
            int hight = world.getHighestBlockYAt(centerX, centerZ) - 10;
            int centerY = random.nextInt(Math.abs(hight)) + 5;
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
