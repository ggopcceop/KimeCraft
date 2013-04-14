package me.kime.kc.Noob;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import me.kime.kc.KC;

public class Noob {

    private KC plugin;
    private World noobWorld;

    public Noob(KC instance) {
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

    public KC getPlugin() {
        return plugin;
    }

    public World getNoobWorld() {
        return noobWorld;
    }
}
