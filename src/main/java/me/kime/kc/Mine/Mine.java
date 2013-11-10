package me.kime.kc.Mine;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;

import me.kime.kc.KimeCraft;
import me.kime.kc.Task.ThreadTask.MinePaymentTask;
import me.kime.kc.Util.KCLogger;
import org.bukkit.Difficulty;
import org.bukkit.WorldType;

public class Mine {

    private KimeCraft plugin;
    private World mineWorld;
    private MinePaymentTask minePaymentTask;
    private final MineDataSource datasource;

    public Mine(KimeCraft instance) {
        this.plugin = instance;

        int max = plugin.getConfig().getInt("mine.mysql.maxconection", 2);
        String host = plugin.getConfig().getString("mine.mysql.host", "localhost");
        String user = plugin.getConfig().getString("mine.mysql.username", "minecraft");
        String pass = plugin.getConfig().getString("mine.mysql.password", "123456");
        String db = plugin.getConfig().getString("mine.mysql.database", "minecraft");
        datasource = new MineDataSource(host, user, pass, db, max, this);

        plugin.getPluginManager().registerEvents(new MineLinstener(this), plugin);

        //delete mine world every Sunday 
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            KCLogger.info("Deleting Mine world!");

            File file = new File(plugin.getServer().getWorldContainer() + File.separator + "MineWorld/");
            deleteDirectory(file);

            KCLogger.info("Regenerating new Mine world!");

        }

        mineWorld = plugin.getServer().createWorld(
                new WorldCreator("MineWorld").environment(Environment.NORMAL).generateStructures(true).type(WorldType.LARGE_BIOMES));

        mineWorld.setSpawnFlags(true, false);
        mineWorld.setDifficulty(Difficulty.HARD);

        minePaymentTask = new MinePaymentTask(plugin);
        plugin.registerTask(minePaymentTask);

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                minePaymentTask.queue();
            }
        }, 305L, 1200L);

        MineCommand mineCommand = new MineCommand(this);
        plugin.getCommand("mine").setExecutor(mineCommand);

    }

    public MineDataSource getDataSource() {
        return datasource;
    }

    public KimeCraft getPlugin() {
        return plugin;
    }

    public World getMineWorld() {
        return mineWorld;
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
}
