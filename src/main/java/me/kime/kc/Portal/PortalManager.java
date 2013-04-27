package me.kime.kc.Portal;

import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class PortalManager {

    private static final int MAX_WIDTH = 4;
    private static final int MAX_HIGHT = 6;
    private static final BlockFace[] axis = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private static final BlockFace[] radial = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};

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
        System.out.println("ererer");
        return block;
    }

    private static Block findBaseBlock2(Block block) {
        while (block.getTypeId() == 90) {
            block = block.getRelative(BlockFace.DOWN);
        }
        return block;
    }

    public static boolean setTo(int typeId, Location from, Location to, TravelAgent agent, Portal portal) {
        Block block;
        Location newto, loc;
        switch (typeId) {
            case 49:
                newto = from.clone();
                newto.setWorld(portal.getPlugin().getNether());
                if (from.getWorld() == newto.getWorld()) {
                    return false;
                }
                agent.setCanCreatePortal(false);
                loc = agent.findPortal(newto);
                if (loc != null) {
                    setToLoc(loc, to);
                } else {
                    BlockFace face = yawToFace(from.getYaw());
                    createPort(newto, 45, (face == BlockFace.SOUTH || face == BlockFace.NORTH));
                    setToLoc(newto, to);
                }
                return true;
            case 48:
                loc = portal.getPlugin().getMine().getMineWorld().getSpawnLocation();
                if (from.getWorld() == loc.getWorld()) {
                    return false;
                }
                setToLoc(loc, to);
                return true;
            case 45:
                if (from.getWorld() == portal.getPlugin().getMine().getMineWorld()) {
                    loc = portal.getPlugin().getCity();
                    setToLoc(loc, to);
                } else {
                    newto = from.clone();
                    newto.setWorld(portal.getPlugin().getDefaultWorld());
                    if (from.getWorld() == newto.getWorld()) {
                        return false;
                    }
                    loc = agent.findPortal(newto);
                    if (loc != null) {
                        setToLoc(loc, to);
                    } else {
                        block = portal.getPlugin().getDefaultWorld().getHighestBlockAt(newto);
                        loc = block.getLocation();
                        setToLoc(loc, to);
                    }
                }
                return true;
            default:
                return false;

        }
    }

    private static void setToLoc(Location form, Location to) {
        to.setX(form.getX());
        to.setY(form.getY());
        to.setZ(form.getZ());
        to.setWorld(form.getWorld());
        to.setYaw(form.getYaw());
    }

    private static void createPort(Location loc, int frameId, boolean north) {
        Block tmp = loc.getBlock();
        int[][] port = new int[][]{{1, 1, 1, 1}, {1, 2, 2, 1}, {1, 2, 2, 1}, {1, 2, 2, 1}, {1, 1, 1, 1}};
        for (int h = 0; h < 5; h++) {
            for (int w = 0; w < 4; w++) {
                if (port[h][w] == 2) {
                    tmp.setTypeIdAndData(90, (byte) 0, false);
                }
                if (port[h][w] == 1) {
                    tmp.setTypeIdAndData(frameId, (byte) 0, false);
                }
                if (!north) {
                    tmp.getRelative(BlockFace.EAST).setTypeIdAndData(0, (byte) 0, false);
                    tmp.getRelative(BlockFace.WEST).setTypeIdAndData(0, (byte) 0, false);
                    tmp = tmp.getRelative(BlockFace.SOUTH);
                } else {
                    tmp.getRelative(BlockFace.SOUTH).setTypeIdAndData(0, (byte) 0, false);
                    tmp.getRelative(BlockFace.NORTH).setTypeIdAndData(0, (byte) 0, false);
                    tmp = tmp.getRelative(BlockFace.EAST);
                }
            }
            tmp = loc.getBlock().getRelative(BlockFace.UP, (h + 1));
        }
    }

    /**
     * Gets the horizontal Block Face from a given yaw angle<br>
     * This includes the NORTH_WEST faces
     *
     * @param yaw angle
     * @return The Block Face of the angle
     */
    private static BlockFace yawToFace(float yaw) {
        return yawToFace(yaw, true);
    }

    /**
     * Gets the horizontal Block Face from a given yaw angle
     *
     * @param yaw angle
     * @param useSubCardinalDirections setting, True to allow NORTH_WEST to be
     * returned
     * @return The Block Face of the angle
     */
    private static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections) {
            return radial[Math.round(yaw / 45f) & 0x7];
        } else {
            return axis[Math.round(yaw / 90f) & 0x3];
        }
    }
}
