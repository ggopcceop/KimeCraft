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
package me.kime.kc.mine;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import me.kime.kc.task.threadTask.MinePaymentTask;
import me.kime.kc.util.KLogger;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class Mine extends Addon {

    private World mineWorld;
    private MinePaymentTask minePaymentTask;
    private MineDataSource datasource;

    public Mine(KimeCraft instance) {
        super(instance);
    }

    @Override
    public String getAddonName() {
        return "Mine";
    }

    @Override
    public void onEnable() {
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
            KLogger.info("Deleting Mine world!");

            File file = new File(plugin.getServer().getWorldContainer() + File.separator + "MineWorld/");
            deleteDirectory(file);

            KLogger.info("Regenerating new Mine world!");

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

    @Override
    public void onDisable() {
    }

    public MineDataSource getDataSource() {
        return datasource;
    }

    public World getMineWorld() {
        return mineWorld;
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return (path.delete());
    }

}
