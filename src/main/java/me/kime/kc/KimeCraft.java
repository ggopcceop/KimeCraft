package me.kime.kc;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import me.kime.kc.Admin.Admin;

import me.kime.kc.Auth.Auth;
import me.kime.kc.ChopTree.ChopTree;
import me.kime.kc.Fun.Fun;
import me.kime.kc.Lander.Lander;
import me.kime.kc.Mine.Mine;
import me.kime.kc.Noob.Noob;
import me.kime.kc.Portal.Portal;
import me.kime.kc.SignTP.SignTP;
import me.kime.kc.Task.ThreadTask.Task;
import me.kime.kc.Task.ThreadTask.ThreadManager;
import me.kime.kc.Util.KCLogFilter;
import me.kime.kc.Util.KCLogger;
import net.milkbowl.vault.economy.Economy;
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
    private static HashMap<String, KPlayer> onlineList = new HashMap<>();
    private World world;
    private World nether;
    private World end;
    /* modules of KimeCraft plugin */
    private Admin admin;
    private Auth auth;
    private Fun fun;
    private Lander lander;
    private Noob noob;
    private SignTP signTP;
    private ChopTree chopTree;
    private Portal portal;
    private Mine mine;
    private Random rand;
    private static Economy economy = null;
    private FileConfiguration config;

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
        KCLogger.info("KimeCraft! Disabled");
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

        if (config.getBoolean("admin.enable", true)) {
            admin = new Admin(this);
        }
        if (config.getBoolean("noob.enable", true)) {
            noob = new Noob(this);
        }
        if (config.getBoolean("auth.enable", true)) {
            auth = new Auth(this);
        }
        if (config.getBoolean("fun.enable", true)) {
            fun = new Fun(this);
        }
        if (config.getBoolean("signTP.enable", true)) {
            signTP = new SignTP(this);
        }
        if (config.getBoolean("chopTree.enable", true)) {
            chopTree = new ChopTree(this);
        }
        if (config.getBoolean("portal.enable", true)) {
            portal = new Portal(this);
        }
        if (config.getBoolean("mine.enable", true)) {
            mine = new Mine(this);
        }

        this.getServer().getLogger().setFilter(new KCLogFilter());

        getServer().getLogger().setFilter(new KCLogFilter());

        getPluginManager().registerEvents(new KCLinstener(this, onlineList), this);

        KCCommand kcCommand = new KCCommand(this);
        getCommand("city").setExecutor(kcCommand);

        KCLogger.info("KimeCraft Loaded!");
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

    public Auth getAuth() {
        return auth;
    }

    public Fun getFun() {
        return fun;
    }

    public Lander getLander() {
        return lander;
    }

    public Noob getNoob() {
        return noob;
    }

    public SignTP getSignTP() {
        return signTP;
    }

    public ChopTree getChopTree() {
        return chopTree;
    }

    public Mine getMine() {
        return mine;
    }

    public Portal getPortal() {
        return portal;
    }

    public void registerTask(Task task) {
        threadManager.registerTask(task);
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
