package me.kime.kc.Task.ThreadTask;

import java.util.LinkedList;
import me.kime.kc.SignTP.SignTP;
import me.kime.kc.SignTP.SignTpDataSource;
import me.kime.kc.Util.KCMessager;
import me.kime.kc.Util.KCTPer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Kime
 */
public class SignTPTask extends Task {

    private LinkedList<subTask> list;
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
                    KCMessager.sentMessage(t.player, "No Hub found by that name.", ChatColor.BLUE);
                } else {
                    final Location location = loc;
                    signtp.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(signtp.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            if (!KCTPer.tp(t.player, location)) {
                                KCMessager.sentMessage(t.player, "Dont have enough space", ChatColor.RED);
                            }
                        }
                    });
                }

            } else if (t.type == 1) {
                if (dataSource.insertLocation(t.player.getLocation(), t.name)) {
                    t.player.sendMessage("Added " + t.name + " to the Hub List");
                } else {
                    t.player.sendMessage("That hub already exists, please destroy the old one first.");
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
