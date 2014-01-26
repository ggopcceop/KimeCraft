/*
 * Copyright (C) 2014 Kime
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.kime.kc;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import me.kime.kc.admin.Admin;
import me.kime.kc.chopTree.ChopTree;
import me.kime.kc.fun.Fun;
import me.kime.kc.mine.Mine;
import me.kime.kc.noob.Noob;
import me.kime.kc.portal.Portal;
import me.kime.kc.signTP.SignTP;
import me.kime.kc.auth.Auth;
import me.kime.kc.chat.Chat;
import me.kime.kc.motd.MOTD;
import me.kime.kc.task.threadTask.Task;
import me.kime.kc.task.threadTask.ThreadManager;
import me.kime.kc.util.KLogFilter;
import me.kime.kc.util.KLogger;
import net.milkbowl.vault.economy.Economy;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * KimeCraft plugin
 *
 * @author Kime
 *
 */
public class KimeCraft extends JavaPlugin {

    private Executor pool;
    private ThreadManager threadManager;
    private static final HashMap<String, KPlayer> onlineList = new HashMap<>();
    private World world;
    private World nether;
    private World end;
    private Random rand;
    private static Economy economy = null;
    public FileConfiguration config;

    @Override
    public void onDisable() {

        long time = System.currentTimeMillis();
        while (!threadManager.isDone()) {
            long curr = System.currentTimeMillis();
            if (curr - time > 10000) {
                break;
            }
        }

        //pool.close();
        KLogger.info("KimeCraft! Disabled");
    }

    @Override
    public void onEnable() {
        //config
        config();

        //thread pool for maxmize cpu performce
        pool = Executors.newFixedThreadPool(8);
        threadManager = new ThreadManager(pool);
        threadManager.start();

        rand = new Random();

        setupEconomy();

        world = getServer().getWorld("world");
        nether = getServer().getWorld("world_nether");
        nether.setDifficulty(Difficulty.HARD);
        end = getServer().getWorld("world_the_end");
        end.setDifficulty(Difficulty.HARD);

        //enable addons
        registerAddon(new Admin(this));
        registerAddon(new Noob(this));
        registerAddon(new Auth(this));
        registerAddon(new MOTD(this));
        registerAddon(new Chat(this));
        registerAddon(new Fun(this));
        registerAddon(new SignTP(this));
        registerAddon(new ChopTree(this));
        registerAddon(new Portal(this));
        registerAddon(new Mine(this));

        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new KLogFilter());

        getPluginManager().registerEvents(new KCListener(this, onlineList), this);

        KCCommand kcCommand = new KCCommand(this);
        getCommand("city").setExecutor(kcCommand);

        KLogger.info("KimeCraft Loaded!");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    private void config() {
        this.saveDefaultConfig();
        config = this.getConfig();
    }

    public KPlayer getOnlinePlayer(String name) {
        return onlineList.get(name.toLowerCase());
    }

    public PluginManager getPluginManager() {
        return getServer().getPluginManager();
    }

    //======== thread tasks =======//
    public void registerTask(Task task) {
        threadManager.registerTask(task);
    }

    //==== addons ======//
    private final HashMap<String, Addon> Addons = new HashMap<>();

    public void registerAddon(Addon addon) {
        if (addon != null) {
            String name = addon.getAddonName().toLowerCase();
            boolean enable = config.getBoolean(name + ".enable", false);
            if (enable) {
                addon.onEnable();
                Addons.put(name, addon);
            }
        }
    }

    public void reloadAddons() {
        for (String name : Addons.keySet()) {
            Addon addon = Addons.get(name);
            addon.onReload();
        }
    }

    public Addon getAddon(String name) {
        if (name != null) {
            return Addons.get(name.toLowerCase());
        }
        return null;
    }

    public Location getCity() {
        return new Location(world, -308, 216, 22, 0, 0);
    }

    public World getDefaultWorld() {
        return world;
    }

    public World getNether() {
        return nether;
    }

    public World getEnd() {
        return end;
    }

    public Random getRandom() {
        return rand;
    }

    public Economy getEconomy() {
        return economy;
    }
}
