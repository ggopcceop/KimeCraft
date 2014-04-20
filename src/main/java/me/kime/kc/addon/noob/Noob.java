package me.kime.kc.addon.noob;

import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import me.kime.kc.task.FireworkDelayTask;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;

public class Noob extends Addon {

    private World noobWorld;
    private final NoobListener noobListener;
    private final NoobCommand noobCommand;
    private CommandExecutor defaultPUCommand;
    private CommandExecutor defaultPDCommand;

    public Noob(KimeCraft instance) {
        super(instance);
        noobListener = new NoobListener(this);
        noobCommand = new NoobCommand(this);
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

        defaultPUCommand = plugin.getCommand("pu").getExecutor();
        defaultPDCommand = plugin.getCommand("pd").getExecutor();

        plugin.getCommand("pu").setExecutor(noobCommand);
        plugin.getCommand("pd").setExecutor(noobCommand);

        plugin.getPluginManager().registerEvents(noobListener, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(noobListener);
        
        plugin.getCommand("pu").setExecutor(defaultPUCommand);
        plugin.getCommand("pd").setExecutor(defaultPDCommand);
    }

    @Override
    public void onReload() {

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
