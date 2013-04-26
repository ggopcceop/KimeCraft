package me.kime.kc.Database;

import biz.source_code.miniConnectionPoolManager.MiniConnectionPoolManager;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.kime.kc.Util.KCLogger;

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

    public void close() {
        try {
            pool.dispose();
        } catch (SQLException ex) {
            KCLogger.showError(ex.getMessage());
        }
    }

    protected void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                KCLogger.showError(e.getMessage());
            }
        }
    }

    protected void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                KCLogger.showError(e.getMessage());
            }
        }
    }

    protected void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                KCLogger.showError(e.getMessage());
            }
        }
    }
}
