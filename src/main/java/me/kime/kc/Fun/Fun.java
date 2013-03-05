package me.Kime.KC.Fun;

import java.util.HashMap;

import org.bukkit.Location;

import me.Kime.KC.KC;
import me.Kime.KC.Task.ThreadTask.RedstoneCounterCleanTask;

public class Fun {

    private KC plugin;
    private HashMap<Location, RedstoneC> redstone;
    private RedstoneCounterCleanTask redstoneCounterCleanTask;

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

        KCraftingRecipe kCraftingRecipe = new KCraftingRecipe(this);

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
