package me.kime.kc.Task.ThreadTask;

import java.util.LinkedList;
import me.kime.kc.Mine.Mine;
import me.kime.kc.Util.KCMessager;
import me.kime.kc.Util.KCTPer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 *
 * @author Kime
 */
public class MineLocationLeaveTask extends Task {

    private final LinkedList<Player> list;
    private final Mine mine;

    public MineLocationLeaveTask(Mine mine) {
        list = new LinkedList<>();
        this.mine = mine;
    }

    @Override
    public void run() {
        final Player player;

        synchronized (lock) {
            if (!list.isEmpty()) {
                player = list.removeFirst();
            } else {
                return;
            }
        }

        if (player != null) {
            final Location loc = mine.getDataSource().checkLogedPlayerLocation(player);
            if (loc != null) {
                mine.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(mine.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        if (!KCTPer.tp(player, loc, TeleportCause.PLUGIN)) {
                            KCMessager.sentMessage(player, "Dont have enough space", ChatColor.RED);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int queueSize() {
        return list.size();
    }

    public void queue(Player player) {
        list.addLast(player);
    }
}
