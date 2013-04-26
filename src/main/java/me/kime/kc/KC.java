package me.kime.kc;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.kime.kc.Auth.Auth;
import me.kime.kc.ChopTree.ChopTree;
import me.kime.kc.Fun.Fun;
import me.kime.kc.Lander.Lander;
import me.kime.kc.Mine.Mine;
import me.kime.kc.Noob.Noob;
import me.kime.kc.SignTP.SignTP;
import me.kime.kc.Task.ThreadTask.Task;
import me.kime.kc.Task.ThreadTask.ThreadManager;
import me.kime.kc.Util.KCLogFilter;
import me.kime.kc.Util.KCLogger;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * KC plugin
 *
 * @author Kime
 *
 */
public class KC extends JavaPlugin {

    private Executor pool;
    private ThreadManager threadManager;
    private static HashMap<String, KPlayer> onlineList = new HashMap<>();
    /* modules of KC plugin */
    private Auth auth;
    private Fun fun;
    private Lander lander;
    private Noob noob;
    private World world;
    private SignTP signTP;
    private ChopTree chopTree;
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

        if (config.getBoolean("noob.enable", true)) {
            noob = new Noob(this);
        }
        if (config.getBoolean("auth.enable", true)) {
            auth = new Auth(this, onlineList);
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

        if (config.getBoolean("mine.enable", true)) {
            mine = new Mine(this);
        }

        this.getServer().getLogger().setFilter(new KCLogFilter());

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

    public void registerTask(Task task) {
        threadManager.registerTask(task);
    }

    public World getDefaultWorld() {
        return world;
    }

    public Random getRandom() {
        return rand;
    }

    public Economy getEconomy() {
        return economy;
    }
}
