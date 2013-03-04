package me.Kime.KC;

import java.util.HashMap;
import java.util.Random;

import me.Kime.KC.Auth.Auth;
import me.Kime.KC.ChopTree.ChopTree;
import me.Kime.KC.Fun.Fun;
import me.Kime.KC.Lander.Lander;
import me.Kime.KC.Mine.Mine;
import me.Kime.KC.Noob.Noob;
import me.Kime.KC.SignTP.SignTP;
import me.Kime.KC.Task.ThreadTask.TTask;
import me.Kime.KC.Task.ThreadTask.ThreadManager;
import me.Kime.KC.Util.KCLogFilter;
import me.Kime.KC.Util.KCLogger;
import me.kime.Threadpool.ThreadPool;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.World;
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

    private ThreadPool pool;
    private ThreadManager threadManager;
    private static HashMap<String, KPlayer> onlineList = new HashMap<String, KPlayer>();
    private Auth auth;
    private Fun fun;
    private Lander lander;
    private Noob noob;
    private World world;
    private SignTP signTP;
    private ChopTree chopTree;
    private Mine mine;
    private Random rand;
    public static Economy economy = null;

    @Override
    public void onDisable() {

        long time = System.currentTimeMillis();
        while (!threadManager.isDone()) {
            long curr = System.currentTimeMillis();
            if (curr - time > 10000) {
                break;
            }
        }

        saveConfig();

        //pool.close();
        KCLogger.info("KimeCraft! Disabled");
    }

    @Override
    public void onEnable() {
        //thread pool for maxmize cpu performce
        pool = new ThreadPool(8, 100);
        threadManager = new ThreadManager(pool);
        threadManager.start();

        rand = new Random();

        setupEconomy();

        world = getServer().getWorld("world");

        noob = new Noob(this);
        auth = new Auth(this);
        fun = new Fun(this);
        signTP = new SignTP(this);
        chopTree = new ChopTree(this);
        mine = new Mine(this);
        //lander = new Lander(this);

        this.getServer().getLogger().setFilter(new KCLogFilter());

        KCCommand kcCommand = new KCCommand(this);
        getCommand("city").setExecutor(kcCommand);

        getServer().getPluginManager().registerEvents(new KCLoginListener(this, onlineList), this);

        KCLogger.info("KimeCraft Loaded!");
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
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

    public void registerTask(TTask task) {
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
