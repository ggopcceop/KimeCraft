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
package me.kime.kc.addon.mine;

import me.kime.kc.KPlayer;
import me.kime.kc.util.KCTPer;
import me.kime.kc.util.KMessager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.WorldInitEvent;

public class MineLinstener implements Listener {

    private final Mine mine;

    public MineLinstener(Mine mine) {
        this.mine = mine;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld() == mine.getMineWorld()) {
            switch (event.getBlock().getType()) {
                case REDSTONE:
                case REDSTONE_WIRE:
                case REDSTONE_TORCH_ON:
                case REDSTONE_TORCH_OFF:
                    event.setCancelled(true);
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if ((event.getPlayer().getWorld() == mine.getPlugin().getDefaultWorld())
                || (event.getPlayer().getWorld() == mine.getMineWorld())) {
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                switch (event.getBlock().getType()) {
                    case STONE:
                    case GRASS:
                    case DIRT:
                    case COBBLESTONE:
                    case GOLD_ORE:
                    case IRON_ORE:
                    case COAL_ORE:
                    case SANDSTONE:
                    case OBSIDIAN:
                    case DIAMOND_ORE:
                    case REDSTONE_ORE:
                    case GLOWING_REDSTONE_ORE:
                    case CLAY:
                    case NETHERRACK:
                    case SOUL_SAND:
                    case GLOWSTONE:
                    case MONSTER_EGGS:
                    case SMOOTH_BRICK:
                    case MYCEL:
                        KPlayer player = mine.getPlugin().getOnlinePlayer(event.getPlayer().getName());
                        if (player.getLastMine() == event.getBlock().getType()) {
                            player.setPayRate(player.getPayRate() * 0.9);
                        } else {
                            player.setPayRate(0.1);
                        }

                        if (player.getPayRate() < 0.01) {
                            player.setPayRate(0);
                        }

                        player.setLastMine(event.getBlock().getType());
                        player.addSalary(player.getPayRate());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        World world = event.getLocation().getWorld();
        if (world == mine.getMineWorld()) {
            if (event.getSpawnReason() == SpawnReason.NATURAL) {
                world.spawnEntity(event.getLocation(), event.getEntityType());
            }
        }
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        if ("MineWorld".equals(event.getWorld().getName())) {
            event.getWorld().getPopulators().add(new QuartzOrePopulator());
            event.getWorld().getPopulators().add(new EnderCrystalPopulator());
            event.getWorld().getPopulators().add(new ArenaPopulator());
            event.getWorld().getPopulators().add(new LobbyPopulator());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld() == mine.getMineWorld()) {
            if (event.getCause() == TeleportCause.NETHER_PORTAL) {
                mine.getDataSource().query("SELECT * FROM kc_mine_player WHERE name=?;", pst -> {
                    pst.setString(1, event.getPlayer().getName());

                }).onDone(r -> {
                    if (r.next()) {
                        World world = mine.getPlugin().getServer().getWorld(r.getString("world"));
                        Location newLoc = new Location(world, r.getDouble("x"), r.getDouble("y"), r.getDouble("z"));
                        if (!KCTPer.tp(event.getPlayer(), newLoc, TeleportCause.PLUGIN)) {
                            KMessager.sendError(mine.getPlugin().getOnlinePlayer(event.getPlayer().getName()), "notSpace");
                        }
                    }
                    r.close();
                }).execute();
                event.setCancelled(true);
            }
        } else if (event.getTo()
                .getWorld() == mine.getMineWorld()) {
            Location location = event.getPlayer().getLocation().clone();
            mine.getDataSource().update("INSERT INTO kc_mine_player (name, world, x, y, z)"
                    + " VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name=values(name),"
                    + " world=VALUES(world), x=VALUES(x), y=VALUES(y), z=VALUES(z);", pst -> {
                        pst.setString(1, event.getPlayer().getName());
                        pst.setString(2, location.getWorld().getName());
                        pst.setDouble(3, location.getX());
                        pst.setDouble(4, location.getY());
                        pst.setDouble(5, location.getZ());
                    }).execute();
        }
    }
}
