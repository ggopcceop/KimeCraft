package me.kime.kc.Task.ThreadTask;

import java.util.LinkedList;
import me.kime.kc.Mine.Mine;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author Kime
 */
public class MineLocationEnterTask extends Task {

    private final LinkedList<SubTask> list;
    private final Mine mine;
    
    public MineLocationEnterTask(Mine mine){
        list = new LinkedList<>();
        this.mine = mine;
    }

    @Override
    public void run() {
        SubTask subTask;

        synchronized (lock) {
            if (!list.isEmpty()) {
                subTask = list.removeFirst();
            } else {
                return;
            }
        }

        if(subTask != null){
            mine.getDataSource().logPlayerLocation(subTask.player, subTask.loc);
        }
    }

    @Override
    public int queueSize() {
        return list.size();
    }

    public void queue(Player player) {        
        list.addLast(new SubTask(player, player.getLocation()));
    }
    
    class SubTask{
        Player player;
        Location loc;
        
        public SubTask(Player player, Location loc){
            this.player = player;
            this.loc = loc;
        }
    }
}
