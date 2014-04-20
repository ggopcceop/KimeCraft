package me.kime.kc.addon.portal;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class PortalManager {

    private static final int MAX_WIDTH = 4;
    private static final int MAX_HIGHT = 6;

    public static boolean isPortalBase(int id) {
        switch (id) {
            case 48:
            case 49:
            case 45:
                return true;
            default:
                return false;
        }
    }

    public static boolean allowedWorld(String world) {
        switch (world) {
            case "world":
            case "MineWorld":
            case "world_nether":
                return true;
            default:
                return false;
        }
    }

    public static void createPortalByFrame(Block block, int blockid) {
        byte facingEastWest = 0;
        byte facingNorthSouth = 0;
        if ((block.getRelative(BlockFace.EAST).getTypeId() == blockid)
                || (block.getRelative(BlockFace.WEST).getTypeId() == blockid)) {
            facingEastWest = 1;

        }

        if ((block.getRelative(BlockFace.NORTH).getTypeId() == blockid)
                || (block.getRelative(BlockFace.SOUTH).getTypeId() == blockid)) {
            facingNorthSouth = 1;

        }

        if (facingEastWest == facingNorthSouth) {
            return;
        }

        BlockFace face1, face2;
        if (facingEastWest == 1) {
            face1 = BlockFace.EAST;
            face2 = BlockFace.WEST;
        } else {
            face1 = BlockFace.NORTH;
            face2 = BlockFace.SOUTH;
        }
        int i = 0;
        Block curr = block.getRelative(BlockFace.UP);
        while (true) {
            if (i >= MAX_HIGHT) {
                return;
            }
            set(curr, face1, blockid, MAX_WIDTH);
            set(curr, face2, blockid, MAX_WIDTH);
            curr = curr.getRelative(BlockFace.UP);
            if (curr.getTypeId() == blockid) {
                return;
            } else if (curr.getTypeId() == 0) {
                i++;
            } else {
                return;
            }
        }

    }

    public static boolean isPortal(Block block, int blockid) {
        byte facingEastWest = 0;
        byte facingNorthSouth = 0;
        if ((block.getRelative(BlockFace.EAST).getTypeId() == blockid)
                || (block.getRelative(BlockFace.WEST).getTypeId() == blockid)) {
            facingEastWest = 1;

        }

        if ((block.getRelative(BlockFace.NORTH).getTypeId() == blockid)
                || (block.getRelative(BlockFace.SOUTH).getTypeId() == blockid)) {
            facingNorthSouth = 1;

        }

        if (facingEastWest == facingNorthSouth) {
            return false;
        }
        BlockFace face1, face2;
        if (facingEastWest == 1) {
            face1 = BlockFace.EAST;
            face2 = BlockFace.WEST;
        } else {
            face1 = BlockFace.NORTH;
            face2 = BlockFace.SOUTH;
        }
        int i = 0;
        Block curr = block.getRelative(BlockFace.UP);
        while (true) {
            if (i >= MAX_HIGHT) {
                return false;
            }
            if (!check(curr, face1, blockid, false, false, MAX_WIDTH)) {
                return false;
            }
            if (!check(curr, face2, blockid, false, false, MAX_WIDTH)) {
                return false;
            }
            curr = curr.getRelative(BlockFace.UP);
            if (curr.getTypeId() == blockid) {
                return true;
            } else if (curr.getTypeId() == 0) {
                i++;
            } else {
                return false;
            }
        }
    }

    private static boolean check(Block thisBlock, BlockFace face, int acceptID, boolean bottom, boolean top, int count) {
        if (count == 0) {
            return false;
        }
        int id;
        id = thisBlock.getRelative(BlockFace.DOWN).getTypeId();
        if (bottom) {
            if (id != acceptID) {
                return false;
            }
        } else {
            if (id == acceptID) {
                bottom = true;
            } else if (id != 0) {
                return false;
            }
        }

        id = thisBlock.getRelative(BlockFace.UP).getTypeId();
        if (top) {
            if (id != acceptID) {
                return false;
            }
        } else {
            if (id == acceptID) {
                top = true;
            } else if (id != 0) {
                return false;
            }
        }

        id = thisBlock.getRelative(face).getTypeId();
        if (id == acceptID) {
            return true;
        } else if (id == 0) {
            return check(thisBlock.getRelative(face), face, acceptID, bottom, top, count--);
        }
        return false;
    }

    private static void set(Block thisBlock, BlockFace face, int stopID, int count) {
        if (count == 0) {
            return;
        }
        int id = thisBlock.getTypeId();
        if (id == 0) {
            thisBlock.setTypeIdAndData(90, (byte) 0, false);
        }
        id = thisBlock.getRelative(face).getTypeId();
        if (id != stopID) {
            set(thisBlock.getRelative(face), face, stopID, count--);
        }
    }

    public static Block findBaseBlock(Block block) {
        Block curr;
        curr = block.getRelative(BlockFace.EAST);
        if (curr.getTypeId() == 90) {
            return findBaseBlock2(curr);
        }
        curr = block.getRelative(BlockFace.SOUTH);
        if (curr.getTypeId() == 90) {
            return findBaseBlock2(curr);
        }
        curr = block.getRelative(BlockFace.WEST);
        if (curr.getTypeId() == 90) {
            return findBaseBlock2(curr);
        }
        curr = block.getRelative(BlockFace.NORTH);
        if (curr.getTypeId() == 90) {
            return findBaseBlock2(curr);
        }
        return block;
    }

    private static Block findBaseBlock2(Block block) {
        while (block.getTypeId() == 90) {
            block = block.getRelative(BlockFace.DOWN);
        }
        return block;
    }
}
