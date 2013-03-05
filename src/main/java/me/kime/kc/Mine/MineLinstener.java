package me.Kime.KC.Mine;

import me.Kime.KC.Task.ThreadTask.MinePayTask;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class MineLinstener implements Listener {

    private Mine mine;
    private MinePayTask minePayTask;

    public MineLinstener(Mine mine) {
        this.mine = mine;

        minePayTask = new MinePayTask(mine, 0.1);

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
}
