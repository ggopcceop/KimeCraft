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
package me.kime.kc.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.kime.kc.database.DataSource;

import me.kime.kc.KPlayer;
import me.kime.kc.util.KLogger;

/**
 * SQL connecter to Auth
 *
 * @author Kime
 *
 */
public class AuthDataSource extends DataSource {

    public AuthDataSource(String host, String user, String pass, String db, int max) {
        super(host, user, pass, db, max);
    }

    public void getAuth(KPlayer kPlayer) {
        String user = kPlayer.player.getName().toLowerCase();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = pool.getValidConnection();
            pst = con.prepareStatement("SELECT pre_ucenter_members.username, "
                    + "pre_ucenter_members.password, pre_ucenter_members.salt, "
                    + "pre_common_member.groupid, pre_ucenter_members.lloginip, "
                    + "pre_ucenter_members.llogindate FROM pre_ucenter_members,"
                    + " pre_common_member  WHERE pre_ucenter_members.username=? "
                    + "AND pre_common_member.username=?;");
            pst.setString(1, user);
            pst.setString(2, user);
            rs = pst.executeQuery();

            if (rs.next()) {
                if (rs.getString("lloginip") == null || rs.getString("lloginip").isEmpty()) {
                    kPlayer.LoginIp = "198.18.0.1";
                } else {
                    kPlayer.LoginIp = rs.getString("lloginip");
                }

                kPlayer.password = rs.getString("password");
                kPlayer.salt = rs.getString("salt");
                kPlayer.groupId = rs.getInt("groupid");
                kPlayer.LoginDate = rs.getLong("llogindate");
            }
        } catch (SQLException ex) {
            KLogger.showError(ex.getMessage());
        } finally {
            close(con);
            close(rs);
            close(pst);
        }

    }

    public boolean updateSession(KPlayer kPlayer) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = pool.getValidConnection();
            pst = con.prepareStatement("UPDATE pre_ucenter_members SET lloginip = ?,"
                    + " llogindate = ? WHERE username = ?;");
            pst.setString(1, kPlayer.player.getAddress().getHostString());
            pst.setLong(2, System.currentTimeMillis());
            pst.setString(3, kPlayer.getNameLowCase());
            pst.executeUpdate();
        } catch (SQLException e) {
            KLogger.showError(e.getMessage());
            return false;
        } finally {
            close(con);
            close(pst);
        }
        return true;
    }
}
