package me.kime.kc.SignTP;

import me.kime.kc.Task.ThreadTask.SignTPTask;

import me.kime.kc.Util.KCMessager;
import me.kime.kc.Util.KCTPer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignTPListener implements Listener {

    private SignTP signTP;
    private final SignTPTask task;

    public SignTPListener(SignTP signTP) {
        this.signTP = signTP;

        task = new SignTPTask(signTP, signTP.getDataSource());
        signTP.getPlugin().registerTask(task);
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
                            task.queue(player, line2, 2);
                        }
                    } else {
                        KCMessager.sentError(player, "You don't have permission to destory TP sign");
                        event.setCancelled(true);
                    }
                } else {
                    KCMessager.sentError(player, "You have to sneak to break the TP sign");
                    event.setCancelled(true);
                }
            }
        }
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
                        if (lines[0].endsWith("branch")) {
                            task.queue(player, lines[1], 0);
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
                    task.queue(player, lines[1], 1);
                }
            } else {
                KCMessager.sentError(player, "You don't have permission to create sign tp");
                event.getBlock().breakNaturally();
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (block.getState() instanceof Sign) {
            Sign sign = (Sign) block.getState();
            String line = sign.getLine(0).toLowerCase();
            if(line.startsWith("signtp") || line.startsWith("itemchest")){
                event.setCancelled(true);
            }
        }

    }
}
