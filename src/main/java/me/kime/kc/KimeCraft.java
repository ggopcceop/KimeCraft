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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.kime.kc.addon.admin.Admin;
import me.kime.kc.addon.auth.Auth;
import me.kime.kc.addon.chat.Chat;
import me.kime.kc.addon.chopTree.ChopTree;
import me.kime.kc.database.DataSourceManager;
import me.kime.kc.database.mysql.MysqlDataSource;
import me.kime.kc.addon.fun.Fun;
import me.kime.kc.addon.mine.Mine;
import me.kime.kc.addon.motd.MOTD;
import me.kime.kc.addon.noob.Noob;
import me.kime.kc.addon.party.Party;
import me.kime.kc.addon.portal.Portal;
import me.kime.kc.addon.signTP.SignTP;
import me.kime.kc.task.async.Caller;
import me.kime.kc.task.async.async;
import me.kime.kc.util.KLogFilter;
import me.kime.kc.util.KLogger;
import net.milkbowl.vault.economy.Economy;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

/**
 * KimeCraft plugin
 *
 * @author Kime
 *
 */
public class KimeCraft extends JavaPlugin {

    private ExecutorService pool;
    private static final HashMap<String, KPlayer> onlineList = new HashMap<>();
    private World world;
    private World nether;
    private World end;
    private Random rand;
    private static Economy economy = null;
    public FileConfiguration config;

    @Override
    public void onDisable() {

        disableAddons();

        //pool.close();
        KLogger.info("KimeCraft! Disabled");
    }

    @Override
    public void onEnable() {
        loadDependency();
        //config
        config();

        //thread pool for maxmize cpu performce
        pool = Executors.newFixedThreadPool(8);
        async.init(pool, this);

        //setup database
        DataSourceManager.init(this);
        ConfigurationSection database = config.getConfigurationSection("database.mysql");
        if (database != null) {
            for (String key : database.getKeys(false)) {
                int max = database.getInt(key + ".maxconection", 2);
                String host = database.getString(key + ".host", "localhost");
                String user = database.getString(key + ".username", "minecraft");
                String pass = database.getString(key + ".password", "123456");
                String db = database.getString(key + ".database", "minecraft");

                DataSourceManager.createMysqlDataSource(key, host, user, pass, db, max);
                KLogger.info("Connecting to mysql key " + key);
            }
        }

        database = config.getConfigurationSection("database.mongodb");
        if (database != null) {
            for (String key : database.getKeys(false)) {
                int max = database.getInt(key + ".maxconection", 2);
                String host = database.getString(key + ".host", "localhost");
                String user = database.getString(key + ".username", "minecraft");
                String pass = database.getString(key + ".password", "123456");
                String db = database.getString(key + ".database", "minecraft");

                DataSourceManager.createMongodbDataSource(key, host, user, pass, db, max);
                KLogger.info("Connecting to mongodb key " + key);
            }
        }

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
        registerAddon(new Party(this));

        setupDynmap();

        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addFilter(new KLogFilter());

        getPluginManager().registerEvents(new KCListener(this, onlineList), this);

        KCCommand kcCommand = new KCCommand(this);
        getCommand("city").setExecutor(kcCommand);
        getCommand("kimecraft").setExecutor(kcCommand);

        KLogger.info("KimeCraft Loaded!");

        MysqlDataSource data = DataSourceManager.getMysqlDataSource("test");

        data.query(q -> {
            return q.prepareStatement("SELECT * FROM ttad").executeQuery();
        }).onDone(r -> {
            while (r.next()) {
                System.out.print(r.getString("name") + " " + r.getString("emmm") + " ");
            }
            System.out.println();
        }).execute();

        async.on().call(() -> {
            System.out.println("Hello");
        }).done(() -> {
            System.out.println("World");
        }).execute();

        async.on().call(t -> {
            t.put("1", "Worldsx");
            System.out.println("Hellosx");
        }).done(t -> {
            System.out.println(t.get("1"));
        }).execute();

        async.on("Test1").call(t -> {
            t.put("1", "111");
            t.put("1", "222");
            System.out.println("Hellosx3");
        }).done(t -> {
            System.out.println("World 3 " + t.get("1"));
        });

        Caller caller = async.listen("Test1");
        caller.call();
        caller.call();

        async.on().execute();

    }

    private void loadDependency() {
        KLogger.info("Loading Drivers!");
        File dependency = new File("plugins/KimeCraft/mongo-java-driver.jar");

        if (!dependency.exists()) {
            try {
                URL website = new URL("http://central.maven.org/maven2/org/mongodb/mongo-java-driver/2.12.0/mongo-java-driver-2.12.0.jar");
                try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream(dependency)) {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                } catch (IOException ex) {
                    KLogger.showError("Error while downloading drivers");
                }
            } catch (MalformedURLException ex) {
                KLogger.showError("Error while downloading drivers");
            }
        }

        try {
            URLClassLoader urlbukkitloader = (URLClassLoader) getClassLoader();
            final Class<URLClassLoader> sysclass = URLClassLoader.class;
            Method method = sysclass.getDeclaredMethod("addURL", new Class[]{URL.class});

            method.setAccessible(
                    true);
            method.invoke(urlbukkitloader,
                    new Object[]{dependency.toURI().toURL()
                    }
            );
            KLogger.info(
                    " Drivers Loaded!");
        } catch (MalformedURLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            KLogger.showError("Error while loading drivers");

        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class
        );
        if (economyProvider
                != null) {
            economy = economyProvider.getProvider();
        }

        return (economy
                != null);
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
    //======== dynmap  ==========//
    public boolean dynampIntegration = false;
    private DynmapAPI dynmap;

    public boolean setupDynmap() {
        Plugin p = getPluginManager().getPlugin("dynmap");
        if (p != null && p.isEnabled()) {
            setDynmap((DynmapAPI) p);
            dynampIntegration = true;
            Addon chat = getAddon("chat");
            if (chat != null) {
                ((Chat) chat).registerDynmapChatEvent();
            }
        }
        return dynampIntegration;
    }

    public DynmapAPI getDynmap() {
        return dynmap;
    }

    public void setDynmap(DynmapAPI dynmap) {
        this.dynmap = dynmap;
    }

    //==== addons ======//
    private final HashMap<String, Addon> Addons = new HashMap<>();

    public void registerAddon(Addon addon) {
        if (addon != null) {
            String name = addon.getAddonName().toLowerCase();
            boolean enable = config.getBoolean(name + ".enable", false);
            if (enable) {
                addon.onEnable();
                addon.enable = true;
            }
            Addons.put(name, addon);
        }
    }

    public void reloadAddons() {
        this.reloadConfig();
        config = this.getConfig();

        Addons.keySet().stream().forEach((name) -> {
            Addon addon = Addons.get(name);
            boolean enable = config.getBoolean(name + ".enable", false);
            if (addon.enable) {
                if (!enable) {
                    addon.onDisable();
                    addon.enable = false;
                } else {
                    addon.onReload();
                }
            } else {
                if (enable) {
                    addon.onEnable();
                    addon.enable = true;
                }
            }
        });
    }

    public Addon getAddon(String name) {
        if (name != null) {
            return Addons.get(name.toLowerCase());
        }
        return null;
    }

    private void disableAddons() {
        Addons.keySet().stream().map((name) -> Addons.get(name)).filter((addon) -> (addon.enable)).map((addon) -> {
            addon.onDisable();
            return addon;
        }).forEach((addon) -> {
            addon.enable = false;
        });
    }

    /* ======== others ===============*/
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
