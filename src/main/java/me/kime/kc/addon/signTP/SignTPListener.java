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
package me.kime.kc.addon.signTP;

import java.sql.PreparedStatement;
import me.kime.kc.util.KCTPer;
import me.kime.kc.util.KMessager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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

    public SignTPListener(SignTP signTP) {
        this.signTP = signTP;
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
                            signTP.getDataSource().update(c -> {
                                try (PreparedStatement pst = c.prepareStatement("DELETE FROM kc_signtp WHERE name = ?")) {
                                    pst.setString(1, line2.toLowerCase());
                                    pst.executeUpdate();
                                    player.sendMessage("Removed " + line2 + " from the Hub List");
                                }
                            }).execute();
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
                            signTP.getDataSource().query(c -> {
                                try (PreparedStatement pst = c.prepareStatement("SELECT * FROM kc_signtp WHERE name = ?")) {
                                    pst.setString(1, lines[1]);
                                    return pst.executeQuery();
                                }
                            }).onDone(rs -> {
                                if (rs.next()) {
                                    String worldname = rs.getString("world");
                                    double x = rs.getDouble("x");
                                    double y = rs.getDouble("y");
                                    double z = rs.getDouble("z");
                                    double yaw = rs.getDouble("yaw");
                                    World world = signTP.getPlugin().getServer().getWorld(worldname);
                                    Location loc = new Location(world, x, y, z, (float) yaw, 0);

                                    if (!KCTPer.tp(player, loc)) {
                                        KMessager.sendError(signTP.getPlugin().getOnlinePlayer(lines[1]), "notSpace");
                                    }
                                } else {
                                    KMessager.sendError(signTP.getPlugin().getOnlinePlayer(player.getName()), "notFound", lines[1]);
                                }
                                rs.close();
                            }).execute();
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
                    Location loc = player.getLocation().clone();
                    signTP.getDataSource().update(c -> {
                        try (PreparedStatement pst = c.prepareStatement("INSERT INTO kc_signtp values(DEFAULT, ?, ?, ?, ?, ?, ?)")) {
                            pst.setString(1, lines[1]);
                            pst.setString(2, loc.getWorld().getName());
                            pst.setDouble(3, loc.getX());
                            pst.setDouble(4, loc.getY());
                            pst.setDouble(5, loc.getZ());
                            pst.setDouble(6, loc.getYaw() + 180F);
                            pst.executeUpdate();

                            KMessager.sendMessage(signTP.getPlugin().getOnlinePlayer(player.getName()), ChatColor.GREEN, "signtp_createHub", lines[1]);
                        }
                    }).onError(e -> {
                        KMessager.sendError(signTP.getPlugin().getOnlinePlayer(player.getName()), "signtp_hubAlreadyExist");
                    }).execute();
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
