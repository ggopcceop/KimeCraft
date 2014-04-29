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
package me.kime.kc.addon.mine;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import me.kime.kc.Addon;
import me.kime.kc.KPlayer;
import me.kime.kc.KimeCraft;
import me.kime.kc.database.DataSourceManager;
import me.kime.kc.database.mysql.MysqlDataSource;
import me.kime.kc.task.async.Caller;
import me.kime.kc.task.async.async;
import me.kime.kc.util.KLogger;
import me.kime.kc.util.KMessager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class Mine extends Addon {

    private World mineWorld;
    private String databaseKey;
    private final MineLinstener mineListener;
    private int minePayTask;
    private final MineCommand mineCommand;
    private MysqlDataSource dataSource;

    public Mine(KimeCraft instance) {
        super(instance);
        mineListener = new MineLinstener(this);
        mineCommand = new MineCommand(this);
    }

    @Override
    public String getAddonName() {
        return "Mine";
    }

    @Override
    public void onEnable() {

        databaseKey = plugin.getConfig().getString("mine.mysql", "minecraft");
        dataSource = DataSourceManager.getMysqlDataSource(databaseKey);
        if (dataSource == null) {
            KLogger.showError("Mine fail to get data source " + databaseKey);
        }
        try {
            dataSource.execute("CREATE TABLE IF NOT EXISTS `kc_mine_player` (\n"
                    + "  `name` varchar(16) NOT NULL,\n"
                    + "  `world` varchar(20) NOT NULL,\n"
                    + "  `x` double NOT NULL,\n"
                    + "  `y` double NOT NULL,\n"
                    + "  `z` double NOT NULL,\n"
                    + "  PRIMARY KEY (`name`)\n"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;", pst -> {
                        pst.executeUpdate();
                    });
        } catch (Exception ex) {
            KLogger.showError(ex.getMessage());
        }

        plugin.getPluginManager().registerEvents(mineListener, plugin);

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

        DecimalFormat format = new DecimalFormat("#.##");
        Caller caller = async.call().call(t -> {
            Player[] players = plugin.getServer().getOnlinePlayers();
            for (Player player1 : players) {
                KPlayer player = plugin.getOnlinePlayer(player1.getName());
                if (player.getSalary(false) > 0) {
                    double mount = player.getSalary(true);
                    Economy eco = plugin.getEconomy();
                    if (eco != null) {
                        eco.depositPlayer(player1.getName(), mount);
                    }
                    if (mount >= 1) {
                        KMessager.sendMessage(player, ChatColor.AQUA, "mine_salaryEarned", format.format(mount));
                    }
                }
            }
        }).caller();

        minePayTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, caller::call, 305L, 1200L);

        plugin.getCommand("mine").setExecutor(mineCommand);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(mineListener);
        plugin.getServer().getScheduler().cancelTask(minePayTask);
    }

    @Override
    public void onReload() {
        reloadConfig();
    }

    private void reloadConfig() {
        databaseKey = plugin.getConfig().getString("mine.mysql", "minecraft");
    }

    public MysqlDataSource getDataSource() {
        return dataSource;
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
