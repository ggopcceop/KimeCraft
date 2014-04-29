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
package me.kime.kc.database.mysql;

import biz.source_code.miniConnectionPoolManager.MiniConnectionPoolManager;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.kime.kc.KimeCraft;
import me.kime.kc.database.QueryType;
import me.kime.kc.database.functionInterface.Update;
import me.kime.kc.util.KLogger;

/**
 *
 * @author Kime
 */
public class MysqlDataSource {

    protected final MiniConnectionPoolManager pool;

    public MysqlDataSource(KimeCraft plugin, String host, String user, String pass, String db, int maxConnections) {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(host);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
        dataSource.setDatabaseName(db);
        pool = new MiniConnectionPoolManager(dataSource, maxConnections);
    }

    public Connection getConnection() {
        return pool.getValidConnection();
    }

    public void close() {
        try {
            pool.dispose();
        } catch (SQLException ex) {
            KLogger.showError(ex.getMessage());
        }
    }

    public MysqlResult query(String sql, Update<PreparedStatement> request) {
        MysqlResult result = new MysqlResult(request, this, QueryType.QUERY, sql);

        return result;
    }

    public MysqlResult update(String sql, Update<PreparedStatement> request) {
        MysqlResult result = new MysqlResult(request, this, QueryType.UPDATE, sql);

        return result;
    }

    public void execute(String sql, Update<PreparedStatement> request) throws Exception {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = pool.getValidConnection();
            pst = con.prepareStatement(sql);
            request.apply(pst);
        } catch (Exception ex) {

        } finally {
            close(con);
            close(pst);
        }
    }

    public void close(AutoCloseable close) {
        try {
            if (close != null) {
                close.close();
            }
        } catch (Exception ex) {
        }
    }

}
