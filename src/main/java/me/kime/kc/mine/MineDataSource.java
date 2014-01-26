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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.kime.kc.database.DataSource;
import me.kime.kc.util.KLogger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Kime
 */
public class MineDataSource extends DataSource {
    private final Mine mine;

    public MineDataSource(String host, String user, String pass, String db, int max, Mine plugin) {
        super(host, user, pass, db, max);
        mine = plugin;
    }

    public void logPlayerLocation(Player player, Location loc) {
        String user = player.getName().toLowerCase();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = pool.getValidConnection();
            pst = con.prepareStatement("INSERT INTO kc_mine_player (name, world, x, y, z)"
                    + " VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name=values(name),"
                    + " world=VALUES(world), x=VALUES(x), y=VALUES(y), z=VALUES(z);");
            pst.setString(1, user);
            pst.setString(2, loc.getWorld().getName());
            pst.setDouble(3, loc.getX());
            pst.setDouble(4, loc.getY());
            pst.setDouble(5, loc.getZ());
            pst.executeUpdate();

        } catch (SQLException ex) {
            KLogger.showError(ex.getMessage());
        } finally {
            close(con);
            close(rs);
            close(pst);
        }
    }

    public Location checkLogedPlayerLocation(Player player) {
        String user = player.getName().toLowerCase();
        
        Location newLoc = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = pool.getValidConnection();
            pst = con.prepareStatement("SELECT * FROM kc_mine_player WHERE name=?;");
            pst.setString(1, user);
            rs = pst.executeQuery();

            if (rs.next()) {
                World world = mine.getPlugin().getServer().getWorld(rs.getString("world"));
                 newLoc = new Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
            }

        } catch (SQLException ex) {
            KLogger.showError(ex.getMessage());
        } finally {
            close(con);
            close(rs);
            close(pst);
        }
        
        return newLoc;
    }
}
