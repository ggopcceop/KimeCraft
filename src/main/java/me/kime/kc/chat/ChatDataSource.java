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
package me.kime.kc.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.kime.kc.KPlayer;
import me.kime.kc.database.DataSource;
import me.kime.kc.database.DataSourceManager;
import me.kime.kc.util.KLogger;

/**
 *
 * @author Kime
 */
public class ChatDataSource {

    private final DataSource dataSource;

    public ChatDataSource(String key, Chat addon) {
        dataSource = DataSourceManager.getDataSource(key);
        if (dataSource == null) {
            KLogger.showError("Chat fail to get data source " + key);
        }
    }

    public void saveCurrentChannel(String name, int currentChannel) {

        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO kc_chat_player (name, currentChannel)"
                    + " VALUES (?, ?) ON DUPLICATE KEY UPDATE  name=VALUES(name),"
                    + " currentChannel=VALUES(currentChannel);");
            pst.setString(1, name);
            pst.setInt(2, currentChannel);
            pst.executeUpdate();

        } catch (SQLException ex) {
            KLogger.showError(ex.getMessage());
        } finally {
            dataSource.close(con);
            dataSource.close(pst);
        }
    }

    public void getCurrentChannel(KPlayer player) {
        String user = player.player.getName().toLowerCase();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM kc_chat_player WHERE name=?;");
            pst.setString(1, user);
            rs = pst.executeQuery();

            if (rs.next()) {
                player.currentChannel = rs.getInt("currentChannel");
            }

        } catch (SQLException ex) {
            KLogger.showError(ex.getMessage());
        } finally {
            dataSource.close(con);
            dataSource.close(rs);
            dataSource.close(pst);
        }
    }

    public boolean createChannel(Channel channel) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO kc_chat_channel (name, type, owner)"
                    + " VALUES (?, ?, ?);");
            pst.setString(1, channel.name);
            pst.setString(2, channel.type.name());
            pst.setString(3, channel.owner);
            channel.id = pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        } finally {
            dataSource.close(con);
            dataSource.close(pst);
        }
    }
}
