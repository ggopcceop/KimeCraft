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
package me.kime.kc.signTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.kime.kc.database.DataSource;
import me.kime.kc.database.DataSourceManager;
import me.kime.kc.util.KLogger;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Kime
 */
public class SignTpDataSource {

    private final SignTP signtp;
    private final DataSource dataSource;

    public SignTpDataSource(String key, SignTP signtp) {
        dataSource = DataSourceManager.getDataSource(key);
        if (dataSource == null) {
            KLogger.showError("Sign TP fail to get data source " + key);
        }
        this.signtp = signtp;
        
       
    }

    public void initTable() {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `kc_signtp` (\n"
                    + "  `id` int(11) NOT NULL AUTO_INCREMENT,\n"
                    + "  `name` varchar(30) NOT NULL,\n"
                    + "  `world` varchar(20) CHARACTER SET latin1 NOT NULL,\n"
                    + "  `x` double NOT NULL,\n"
                    + "  `y` double NOT NULL,\n"
                    + "  `z` double NOT NULL,\n"
                    + "  `yaw` double NOT NULL,\n"
                    + "  PRIMARY KEY (`id`),\n"
                    + "  UNIQUE KEY `name` (`name`)\n"
                    + ") ENGINE=InnoDB  DEFAULT CHARSET=utf8");
            pst.executeUpdate();
        } catch (SQLException e) {
            KLogger.showError(e.getMessage());
        } finally {
            dataSource.close(conn);
            dataSource.close(pst);
        }
    }

    public Location getLocationByName(String name) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("SELECT * FROM kc_signtp WHERE name = ?");
            pst.setString(1, name);
            rs = pst.executeQuery();
            if (rs.next()) {
                String worldname = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                double yaw = rs.getDouble("yaw");
                World world = signtp.getPlugin().getServer().getWorld(worldname);
                return new Location(world, x, y, z, (float) yaw, 0);
            }
            return null;
        } catch (SQLException e) {
            KLogger.showError(e.getMessage());
            return null;
        } finally {
            dataSource.close(conn);
            dataSource.close(pst);
            dataSource.close(rs);
        }
    }

    public boolean insertLocation(Location loc, String name) {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("INSERT INTO kc_signtp values(DEFAULT, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, name.toLowerCase());
            pst.setString(2, loc.getWorld().getName());
            pst.setDouble(3, loc.getX());
            pst.setDouble(4, loc.getY());
            pst.setDouble(5, loc.getZ());
            pst.setDouble(6, loc.getYaw() + 180F);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            KLogger.showError(e.getMessage());
            return false;
        } finally {
            dataSource.close(conn);
            dataSource.close(pst);
        }
    }

    public void removeLocation(String name) {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("DELETE FROM kc_signtp WHERE name = ?");
            pst.setString(1, name.toLowerCase());
            pst.executeUpdate();
        } catch (SQLException e) {
            KLogger.showError(e.getMessage());
        } finally {
            dataSource.close(conn);
            dataSource.close(pst);
        }
    }
}