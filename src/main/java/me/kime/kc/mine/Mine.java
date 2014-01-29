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
    private String databaseKey;

    public Mine(KimeCraft instance) {
        super(instance);
    }

    @Override
    public String getAddonName() {
        return "Mine";
    }

    @Override
    public void onEnable() {

        databaseKey = plugin.getConfig().getString("mine.mysql", "minecraft");
        datasource = new MineDataSource(databaseKey, this);
        datasource.initTable();

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

    @Override
    public void onReload() {
        reloadConfig();
    }

    private void reloadConfig() {
        databaseKey = plugin.getConfig().getString("mine.mysql", "minecraft");
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
