package me.kime.kc.database;

import biz.source_code.miniConnectionPoolManager.MiniConnectionPoolManager;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.kime.kc.util.KLogger;

/**
 *
 * @author Kime
 */
public class DataSource {

    protected final MiniConnectionPoolManager pool;

    protected DataSource(String host, String user, String pass, String db, int maxConnections) {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(host);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
        dataSource.setDatabaseName(db);
        pool = new MiniConnectionPoolManager(dataSource, maxConnections);
    }

    protected DataSource(MiniConnectionPoolManager pool) {
        this.pool = pool;
    }

    public void close() {
        try {
            pool.dispose();
        } catch (SQLException ex) {
            KLogger.showError(ex.getMessage());
        }
    }

    protected void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                KLogger.showError(e.getMessage());
            }
        }
    }

    protected void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                KLogger.showError(e.getMessage());
            }
        }
    }

    protected void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                KLogger.showError(e.getMessage());
            }
        }
    }
}
