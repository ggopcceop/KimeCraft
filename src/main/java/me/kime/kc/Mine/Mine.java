package me.kime.kc.Mine;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

import me.kime.kc.KC;
import me.kime.kc.Task.ThreadTask.MinePaymentTask;
import org.bukkit.Difficulty;

public class Mine {

    private KC plugin;
    private World mineWorld;
    private MinePaymentTask minePaymentTask;

    public Mine(KC instance) {
        this.plugin = instance;

        mineWorld = plugin.getServer().createWorld(
                new WorldCreator("MineWorld").environment(Environment.NORMAL).generateStructures(true));
        mineWorld.getPopulators().add(new QuartzOrePopulator());
        mineWorld.setSpawnFlags(true, false);
        mineWorld.setDifficulty(Difficulty.HARD);
        
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
