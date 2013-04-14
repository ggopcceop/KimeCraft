package me.kime.kc.Fun;

import java.util.HashMap;

import org.bukkit.Location;

import me.kime.kc.KC;
import me.kime.kc.Task.ThreadTask.RedstoneCounterCleanTask;

public class Fun {

    private KC plugin;
    private HashMap<Location, RedstoneC> redstone;
    private RedstoneCounterCleanTask redstoneCounterCleanTask;
    
    public final int R = 300;
    public final int RR = R * R;
    public final int sRR = (R - 2) * (R - 2);

    public Fun(KC instance) {
        plugin = instance;

        redstone = new HashMap<>();

        redstoneCounterCleanTask = new RedstoneCounterCleanTask(this);
        plugin.registerTask(redstoneCounterCleanTask);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                redstoneCounterCleanTask.queue();
            }
        }, 305L, 6001L);

        FunCommand funCommand = new FunCommand(this);
        plugin.getCommand("skull").setExecutor(funCommand);

        plugin.getPluginManager().registerEvents(new FunLinstener(this), plugin);
    }

    public KC getPlugin() {
        return plugin;
    }

    public HashMap<Location, RedstoneC> getRedstone() {
        return redstone;
    }
}
