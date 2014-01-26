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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 *
 * @author Kime
 */
public class EnderCrystalPopulator extends BlockPopulator {

    private static final int GENERATE_CHANCE = 1;

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (random.nextInt(100) <= GENERATE_CHANCE) {
            int centerX = (chunk.getX() << 4) + random.nextInt(16);
            int centerZ = (chunk.getZ() << 4) + random.nextInt(16);
            int hight = world.getHighestBlockYAt(centerX, centerZ) + 20;
            int centerY = random.nextInt(Math.abs(255 - hight)) + hight - 10;
            createPlatform(world, random, centerX, centerY, centerZ);
        }
    }

    private void createPlatform(World world, Random random, int centerX, int centerY, int centerZ) {
        world.getBlockAt(centerX, centerY, centerZ).setType(Material.BEDROCK);
        world.spawn(new Location(world, centerX + 0.5, centerY, centerZ + 0.5), EnderCrystal.class);
        int r = random.nextInt(5) + 3;
        int rr = r * r;
        Vector center = new BlockVector(centerX, centerY - 1, centerZ);
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= 0; y++) {
                for (int z = -r; z <= r; z++) {
                    if ((x * x) + (y * y) + (z * z) <= rr) {
                        Vector position = center.clone().add(new Vector(x, y, z));
                        world.getBlockAt(position.toLocation(world)).setType(Material.OBSIDIAN);
                    }
                }
            }
        }
    }
    
    private void createOre(World world, Random random, int centerX, int centerY, int centerZ) {
    }
}
