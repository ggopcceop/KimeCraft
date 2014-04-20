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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.kime.kc.KimeCraft;
import me.kime.kc.database.DataSource;
import me.kime.kc.database.functionInterface.Query;
import me.kime.kc.database.functionInterface.Update;
import me.kime.kc.util.KLogger;

/**
 *
 * @author Kime
 */
public class MysqlDataSource implements DataSource<Connection, ResultSet> {

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

    public void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                KLogger.showError(e.getMessage());
            }
        }
    }

    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                KLogger.showError(e.getMessage());
            }
        }
    }

    public void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                KLogger.showError(e.getMessage());
            }
        }
    }

    @Override
    public MysqlResult query(Query<Connection, ResultSet> request) {
        MysqlResult result = new MysqlResult(request, this);

        return result;
    }

    @Override
    public MysqlResult update(Update<Connection> request) {
        return query(t -> {
            request.apply(t);
            return null;
        });
    }

    @Override
    public ResultSet execute(Query<Connection, ResultSet> request) throws Exception {
        ResultSet result;
        try (Connection conn = pool.getConnection()) {
            result = request.apply(conn);
        }
        return result;
    }

}
