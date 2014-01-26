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

import me.kime.kc.task.threadTask.MineLocationEnterTask;
import me.kime.kc.task.threadTask.MineLocationLeaveTask;
import me.kime.kc.task.threadTask.MinePayTask;

import org.bukkit.GameMode;
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
    private final MinePayTask minePayTask;
    private final MineLocationEnterTask mineEnterTask;
    private final MineLocationLeaveTask mineLeaveTask;

    public MineLinstener(Mine mine) {
        this.mine = mine;

        mineEnterTask = new MineLocationEnterTask(mine);
        mineLeaveTask = new MineLocationLeaveTask(mine);
        minePayTask = new MinePayTask(mine, 0.1);

        mine.getPlugin().registerTask(mineEnterTask);
        mine.getPlugin().registerTask(mineLeaveTask);
        mine.getPlugin().registerTask(minePayTask);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld() == mine.getMineWorld()) {
            switch (event.getBlock().getTypeId()) {
                case 331:
                case 55:
                case 75:
                case 76:
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
                switch (event.getBlock().getTypeId()) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 14:
                    case 15:
                    case 16:
                    case 24:
                    case 49:
                    case 56:
                    case 73:
                    case 74:
                    case 82:
                    case 87:
                    case 88:
                    case 89:
                    case 97:
                    case 98:
                    case 110:
                        minePayTask.queue(event.getBlock().getTypeId(), event.getPlayer().getName());
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
                mineLeaveTask.queue(event.getPlayer());
                event.setCancelled(true);
            }
        } else if (event.getTo().getWorld() == mine.getMineWorld()) {
            mineEnterTask.queue(event.getPlayer());
        }
    }
}
