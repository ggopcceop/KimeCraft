package me.kime.kc.Util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class KCTPer {

    public static boolean tp(Player player, Location loc) {
        return tp(player, loc, TeleportCause.PLUGIN);
    }

    public static boolean tp(Player player, Location loc, TeleportCause cause) {
        if (cause == null) {
            cause = TeleportCause.PLUGIN;
        }
        if (hasSpace(loc)) {
            player.teleport(loc, cause);
            return true;
        } else if (hasSpace(loc.add(0, 1, 0))) {
            player.teleport(loc, cause);
            return true;
        } else if (hasSpace(loc.add(0, 1, 0))) {
            player.teleport(loc, cause);
            return true;
        }
        return false;
    }

    private static boolean hasSpace(Location loc) {
        if (!isSolid(loc.getBlock()) && !isSolid(loc.getBlock().getRelative(BlockFace.UP))) {
            return true;
        }
        return false;
    }

    private static boolean isSolid(Block block) {
        switch (block.getTypeId()) {
            case 0:
            case 6:
            case 8:
            case 9:
            case 27:
            case 28:
            case 31:
            case 32:
            case 37:
            case 38:
            case 39:
            case 40:
            case 50:
            case 51:
            case 55:
            case 59:
            case 63:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 72:
            case 75:
            case 76:
            case 77:
            case 78:
            case 83:
            case 106:
            case 111:
            case 115:
            case 131:
            case 132:
            case 141:
            case 142:
            case 143:
                return false;
            default:
                return true;

        }
    }
}
