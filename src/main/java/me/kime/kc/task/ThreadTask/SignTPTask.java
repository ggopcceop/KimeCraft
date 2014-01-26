package me.kime.kc.task.threadTask;

import java.util.LinkedList;
import me.kime.kc.signTP.SignTP;
import me.kime.kc.signTP.SignTpDataSource;
import me.kime.kc.util.KMessager;
import me.kime.kc.util.KCTPer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Kime
 */
public class SignTPTask extends Task {

    private final LinkedList<subTask> list;
    private final SignTpDataSource dataSource;
    private final SignTP signtp;

    public SignTPTask(SignTP signtp, SignTpDataSource dataSource) {
        list = new LinkedList();
        this.signtp = signtp;
        this.dataSource = dataSource;
    }

    @Override
    public int queueSize() {
        return list.size();
    }

    @Override
    public void run() {
        synchronized (lock) {
            final subTask t = list.remove();
            if (t.type == 0) {
                Location loc = dataSource.getLocationByName(t.name);
                if (loc == null) {
                    KMessager.sendError(signtp.getPlugin().getOnlinePlayer(t.player.getName()), "notFound", t.name);
                } else {
                    final Location location = loc;
                    signtp.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(signtp.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            if (!KCTPer.tp(t.player, location)) {
                                KMessager.sendError(signtp.getPlugin().getOnlinePlayer(t.player.getName()), "notSpace");
                            }
                        }
                    });
                }

            } else if (t.type == 1) {
                if (dataSource.insertLocation(t.player.getLocation(), t.name)) {
                    KMessager.sendMessage(signtp.getPlugin().getOnlinePlayer(t.player.getName()), ChatColor.GREEN, "signtp_createHub");
                } else {
                    KMessager.sendError(signtp.getPlugin().getOnlinePlayer(t.player.getName()), "signtp_hubAlreadyExist");
                }
            } else {
                dataSource.removeLocation(t.name);
                t.player.sendMessage("Removed " + t.name + " from the Hub List");
            }
        }
    }

    public void queue(Player player, String name, int type) {
        list.add(new subTask(player, name, type));
    }
}

class subTask {

    public Player player;
    public String name;
    public int type;

    public subTask(Player p, String n, int t) {
        player = p;
        name = n;
        type = t;
    }
}
