package me.Kime.KC.Fun;

import java.util.Collection;
import java.util.HashSet;
import net.minecraft.server.v1_5_R2.Block;
import net.minecraft.server.v1_5_R2.World;

public class PortalManager {

    public boolean isPortal(World world) {
        byte b0 = 0;
        byte b1 = 0;
        int i = 0, j = 0, k = 0;
        if ((world.getTypeId(i - 1, j, k) == Block.OBSIDIAN.id) || (world.getTypeId(i + 1, j, k) == Block.OBSIDIAN.id)) {
            b0 = 1;
        }

        if ((world.getTypeId(i, j, k - 1) == Block.OBSIDIAN.id) || (world.getTypeId(i, j, k + 1) == Block.OBSIDIAN.id)) {
            b1 = 1;
        }

        if (b0 == b1) {
            return false;
        }

        Collection blocks = new HashSet();
        org.bukkit.World bworld = world.getWorld();

        if (world.getTypeId(i - b0, j, k - b1) == 0) {
            i -= b0;
            k -= b1;
        }

        for (int l = -1; l <= 2; l++) {
            for (int i1 = -1; i1 <= 3; i1++) {
                boolean flag = (l == -1) || (l == 2) || (i1 == -1) || (i1 == 3);

                if (((l != -1) && (l != 2)) || ((i1 != -1) && (i1 != 3))) {
                    int j1 = world.getTypeId(i + b0 * l, j + i1, k + b1 * l);

                    if (flag) {
                        if (j1 != Block.OBSIDIAN.id) {
                            return false;
                        }
                        blocks.add(bworld.getBlockAt(i + b0 * l, j + i1, k + b1 * l));
                    } else if ((j1 != 0) && (j1 != Block.FIRE.id)) {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
