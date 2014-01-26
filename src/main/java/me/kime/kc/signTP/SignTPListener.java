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
package me.kime.kc.signTP;

import me.kime.kc.task.threadTask.SignTPTask;

import me.kime.kc.util.KMessager;
import me.kime.kc.util.KCTPer;

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

    private final SignTP signTP;
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
                        KMessager.sendError(signTP.getPlugin().getOnlinePlayer(player.getName()), "signtp_noPermissionDestory");
                        event.setCancelled(true);
                    }
                } else {
                    KMessager.sendError(signTP.getPlugin().getOnlinePlayer(player.getName()), "signtp_sneakDestory");
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
                            KMessager.sendMessage(signTP.getPlugin().getOnlinePlayer(player.getName()), ChatColor.BLUE, "signtp_hub");
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
                                            KMessager.sendError(signTP.getPlugin().getOnlinePlayer(player.getName()), "notSpace");
                                        }
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                            }
                            KMessager.sendError(signTP.getPlugin().getOnlinePlayer(player.getName()), "signtp_notFoundDown");
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
                                            KMessager.sendError(signTP.getPlugin().getOnlinePlayer(player.getName()), "notSpace");
                                        }
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                            }
                            KMessager.sendError(signTP.getPlugin().getOnlinePlayer(player.getName()), "signtp_notFoundUp");
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
            if (line.startsWith("signtp") || line.startsWith("itemchest")) {
                event.setCancelled(true);
            }
        }

    }
}
