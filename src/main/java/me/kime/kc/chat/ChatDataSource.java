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
            channel.id =  pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        } finally {
            dataSource.close(con);
            dataSource.close(pst);
        }
    }
}
