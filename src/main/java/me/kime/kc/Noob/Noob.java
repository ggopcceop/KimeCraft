package me.kime.kc.Noob;

import me.kime.kc.Task.FireworkDelayTask;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import me.kime.kc.KimeCraft;

public class Noob {

    private KimeCraft plugin;
    private World noobWorld;

    public Noob(KimeCraft instance) {
        this.plugin = instance;

        noobWorld = plugin.getServer().createWorld(new WorldCreator("NoobTown").generator("PlotMe"));
        noobWorld.setSpawnFlags(false, false);
        Location loc = noobWorld.getSpawnLocation();
        noobWorld.loadChunk(loc.getChunk().getX(), loc.getChunk().getZ());

        NoobCommand noobCommand = new NoobCommand(this);

        plugin.getCommand("pu").setExecutor(noobCommand);
        plugin.getCommand("pd").setExecutor(noobCommand);

        plugin.getPluginManager().registerEvents(new NoobListener(this), plugin);
    }

    public KimeCraft getPlugin() {
        return plugin;
    }

    public World getNoobWorld() {
        return noobWorld;
    }

    public void playFirework(Location loc, int num) {
        FireworkDelayTask task = new FireworkDelayTask(plugin.getRandom(), loc);
        for (int i = 1; i <= num; i++) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, task, i * 20L);
        }
    }
}
