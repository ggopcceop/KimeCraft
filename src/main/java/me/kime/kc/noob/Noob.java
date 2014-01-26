package me.kime.kc.noob;

import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import me.kime.kc.task.FireworkDelayTask;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class Noob extends Addon {

    private World noobWorld;

    public Noob(KimeCraft instance) {
        super(instance);
    }

    @Override
    public String getAddonName() {
        return "Noob";
    }

    @Override
    public void onEnable() {
        noobWorld = plugin.getServer().createWorld(new WorldCreator("NoobTown").generator("PlotMe"));
        noobWorld.setSpawnFlags(false, false);
        Location loc = noobWorld.getSpawnLocation();
        noobWorld.loadChunk(loc.getChunk().getX(), loc.getChunk().getZ());

        NoobCommand noobCommand = new NoobCommand(this);

        plugin.getCommand("pu").setExecutor(noobCommand);
        plugin.getCommand("pd").setExecutor(noobCommand);

        plugin.getPluginManager().registerEvents(new NoobListener(this), plugin);
    }

    @Override
    public void onDisable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
