package me.kime.kc.SignTP;

import java.util.Iterator;

import me.kime.kc.Util.KCMessager;
import me.kime.kc.Util.KCTPer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignTPListener implements Listener {

    private SignTP signTP;
    private FileConfiguration config;

    public SignTPListener(SignTP signTP) {
        this.signTP = signTP;
        this.config = signTP.getPlugin().getConfig();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block blockBroken = event.getBlock();
        if ((blockBroken.getState() instanceof Sign)) {
            Sign sign = (Sign) blockBroken.getState();
            String line1 = sign.getLine(0).toLowerCase().replace(' ', '_');
            String line2 = sign.getLine(1).toLowerCase().replace(' ', '_');

            if (line1.startsWith("signtp")) {
                if (player.isSneaking()) {
                    if (line1.equals("signtp_up") || line1.equals("signtp_down")) {
                        return;
                    }

                    if (player.hasPermission("kc.admin.sign")) {
                        if (line1.equals("signtp_hub")) {
                            if (config.contains("signs.normal." + line2)) {
                                config.set("signs.normal." + line2, null);
                                player.sendMessage("Removed " + line2 + " from the Hub List");
                                signTP.getPlugin().saveConfig();
                            }
                        }
                    } else {
                        KCMessager.sentError(player, "You don't have permission to destory TP sign");
                        event.setCancelled(true);
                    }
                } else {
                    KCMessager.sentError(player, "You  have to sneak to break the TP sign");
                    event.setCancelled(true);
                }
            }
        } else if (!checkSign(blockBroken, BlockFace.NORTH)
                || !checkSign(blockBroken, BlockFace.SOUTH)
                || !checkSign(blockBroken, BlockFace.WEST)
                || !checkSign(blockBroken, BlockFace.EAST)
                || !checkSign(blockBroken, BlockFace.UP)) {
            KCMessager.sentError(player, "You you have to destory TP sign first");
            event.setCancelled(true);
        }


    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        Iterator<Block> destory = event.blockList().iterator();
        while (destory.hasNext()) {
            Block block = destory.next();
            if (!checkSign(block, BlockFace.NORTH)
                    || !checkSign(block, BlockFace.SOUTH)
                    || !checkSign(block, BlockFace.WEST)
                    || !checkSign(block, BlockFace.EAST)
                    || !checkSign(block, BlockFace.UP)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    private boolean checkSign(Block blockBroken, BlockFace face) {
        Material dir = blockBroken.getRelative(face).getType();
        if (face == BlockFace.UP) {
            if (dir == Material.SIGN_POST) {
                Sign sign = (Sign) blockBroken.getRelative(face).getState();
                if (sign.getLine(0).toLowerCase().startsWith("signtp")) {
                    return false;
                }
            }
        } else if (dir == Material.WALL_SIGN) {
            Sign sign = (Sign) blockBroken.getRelative(face).getState();
            if (((org.bukkit.material.Sign) sign.getData())
                    .getAttachedFace().getOppositeFace() == face
                    && sign.getLine(0).toLowerCase().startsWith("signtp")) {
                return false;
            }


        }
        return true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();

        if (((event.getAction() == Action.LEFT_CLICK_BLOCK) && !player.isSneaking())
                || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if ((clickedBlock.getState() instanceof Sign)) {

                if (player.getWorld() == signTP.getPlugin().getDefaultWorld()) {
                    Sign clickedSign = (Sign) clickedBlock.getState();
                    String[] lines = new String[2];
                    lines[0] = clickedSign.getLine(0).toLowerCase().replace(' ', '_');
                    lines[1] = clickedSign.getLine(1).toLowerCase().replace(' ', '_');

                    if (lines[0].startsWith("signtp")) {
                        World currentWorld = player.getWorld();
                        if (lines[0].endsWith("branch")) {
                            String tpString = "signs.normal." + lines[1];
                            if (config.contains(tpString)) {
                                Location tpLoc = new Location(currentWorld,
                                        config.getDouble(tpString + ".x"),
                                        config.getDouble(tpString + ".y"),
                                        config.getDouble(tpString + ".z"),
                                        (float) config.getDouble(tpString + ".yaw"), 0);

                                if (!KCTPer.tp(player, tpLoc)) {
                                    KCMessager.sentMessage(player, "Dont have enough space", ChatColor.RED);
                                }
                            } else {
                                KCMessager.sentMessage(player, "No Hub found by that name.", ChatColor.BLUE);
                            }
                        } else if (lines[0].endsWith("hub")) {
                            KCMessager.sentMessage(player, "This is a hub sign", ChatColor.BLUE);
                        } else if (lines[0].endsWith("up")) {
                            Block b = clickedBlock;
                            while (b.getY() < 255) {
                                b = b.getRelative(BlockFace.UP);
                                if (b.getState() instanceof Sign) {
                                    Sign upSign = (Sign) b.getState();
                                    String upSignLines = upSign.getLine(0).toLowerCase().replace(' ', '_');
                                    if (upSignLines.startsWith("signtp") && upSignLines.endsWith("down")) {
                                        Location loc = player.getLocation();
                                        loc.setY(b.getY());
                                        if (!KCTPer.tp(player, loc)) {
                                            KCMessager.sentMessage(player, "Dont have enough space", ChatColor.RED);
                                        }
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                            }
                            KCMessager.sentMessage(player, "Down sign not found!", ChatColor.RED);
                        } else if (lines[0].endsWith("down")) {
                            Block b = clickedBlock;
                            while (b.getY() > 0) {
                                b = b.getRelative(BlockFace.DOWN);
                                if (b.getState() instanceof Sign) {
                                    Sign upSign = (Sign) b.getState();
                                    String upSignLines = upSign.getLine(0).toLowerCase().replace(' ', '_');
                                    if (upSignLines.startsWith("signtp") && upSignLines.endsWith("up")) {
                                        Location loc = player.getLocation();
                                        loc.setY(b.getY());
                                        if (!KCTPer.tp(player, loc)) {
                                            KCMessager.sentMessage(player, "Dont have enough space", ChatColor.RED);
                                        }
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                            }
                            KCMessager.sentMessage(player, "Up sign not found!", ChatColor.RED);
                        }
                        event.setCancelled(true);
                    }
                }

            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String[] lines = new String[2];
        lines[0] = event.getLine(0).toLowerCase().replace(' ', '_');

        if (lines[0].startsWith("signtp")) {
            lines[1] = event.getLine(1).toLowerCase().replace(' ', '_');
            Player player = event.getPlayer();

            if (lines[0].equals("signtp_up") || lines[0].equals("signtp_down")) {
                return;
            }

            if (event.getPlayer().hasPermission("kc.admin.sign")) {
                if (lines[0].equals("signtp_hub")) {
                    String tpString = "signs.normal." + lines[1];
                    if (!config.contains("signs.normal." + lines[1])) {
                        config.set(tpString + ".x", Double.valueOf(player.getLocation().getX()));
                        config.set(tpString + ".y", Double.valueOf(player.getLocation().getY()));
                        config.set(tpString + ".z", Double.valueOf(player.getLocation().getZ()));
                        config.set(tpString + ".yaw", Float.valueOf((player.getLocation().getYaw() + 180F)));
                        player.sendMessage("Added " + lines[1] + " to the Hub List");
                        signTP.getPlugin().saveConfig();
                    } else {
                        player.sendMessage("That hub already exists, please destroy the old one first.");
                    }
                }
            } else {
                KCMessager.sentError(player, "You don't have permission to create sign tp");
                event.getBlock().breakNaturally();
                event.setCancelled(true);
            }
        }
    }
}
