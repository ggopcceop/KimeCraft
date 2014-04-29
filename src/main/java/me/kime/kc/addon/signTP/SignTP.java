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
package me.kime.kc.addon.signTP;

import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import me.kime.kc.database.DataSourceManager;
import me.kime.kc.database.mysql.MysqlDataSource;
import me.kime.kc.util.KLogger;
import org.bukkit.event.HandlerList;

public class SignTP extends Addon {

    private String databaseKey;
    private final SignTPListener signTPListener;
    private MysqlDataSource dataSource;

    public SignTP(KimeCraft plugin) {
        super(plugin);
        signTPListener = new SignTPListener(this);
    }

    @Override
    public String getAddonName() {
        return "SignTP";
    }

    @Override
    public void onEnable() {
        loadConfig();

        dataSource = DataSourceManager.getMysqlDataSource(databaseKey);
        if (dataSource == null) {
            KLogger.showError("Sign TP fail to get data source " + databaseKey);
        }

        try {
            dataSource.execute("CREATE TABLE IF NOT EXISTS `kc_signtp` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `name` varchar(30) NOT NULL,\n"
                    + "  `world` varchar(20) CHARACTER SET latin1 NOT NULL,\n"
                    + "  `x` double NOT NULL,\n"
                    + "  `y` double NOT NULL,\n"
                    + "  `z` double NOT NULL,\n"
                    + "  `yaw` double NOT NULL,\n"
                    + "  PRIMARY KEY (`id`),\n"
                    + "  UNIQUE KEY `name` (`name`)\n"
                    + ") ENGINE=InnoDB  DEFAULT CHARSET=utf8", pst -> {
                        pst.executeUpdate();
                    });
        } catch (Exception ex) {
            KLogger.showError(ex.getMessage());
        }

        plugin.getServer().getPluginManager().registerEvents(signTPListener, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(signTPListener);
    }

    @Override
    public void onReload() {
        loadConfig();
    }

    private void loadConfig() {
        databaseKey = plugin.getConfig().getString("signtp.mysql", "minecraft");
    }

    public MysqlDataSource getDataSource() {
        return dataSource;
    }

}
