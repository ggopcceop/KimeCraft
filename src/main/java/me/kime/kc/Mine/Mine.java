package me.Kime.KC.Mine;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

import me.Kime.KC.KC;
import me.Kime.KC.Task.ThreadTask.MinePaymentTask;

public class Mine {

    private KC plugin;
    private World mineWorld;
    private MinePaymentTask minePaymentTask;

    public Mine(KC instance) {
        this.plugin = instance;

        mineWorld = plugin.getServer().createWorld(
                new WorldCreator("MineWorld").environment(Environment.NORMAL).generateStructures(true));
        mineWorld.setSpawnFlags(true, false);
        Location loc = mineWorld.getSpawnLocation();
        mineWorld.loadChunk(loc.getChunk().getX(), loc.getChunk().getZ());

        minePaymentTask = new MinePaymentTask(plugin);
        plugin.registerTask(minePaymentTask);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                minePaymentTask.queue();
            }
        }, 305L, 1200L);

        MineCommand mineCommand = new MineCommand(this);
        plugin.getCommand("mine").setExecutor(mineCommand);

        plugin.getPluginManager().registerEvents(new MineLinstener(this), plugin);
    }

    public KC getPlugin() {
        return plugin;
    }

    public World getMineWorld() {
        return mineWorld;
    }
}
