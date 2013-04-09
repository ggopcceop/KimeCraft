package me.Kime.KC.Auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.Kime.KC.KPlayer;
import me.Kime.KC.Util.KCLogger;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * SQL connecter to Auth
 *
 * @author Kime
 *
 */
public class DataSource {

    private final String database;
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String tableName;
    private final String columnName;
    private final String columnPassword;
    private final String columnIp;
    private final String columnLastLogin;
    
    private Connection connection;

    public DataSource(FileConfiguration config) throws ClassNotFoundException, SQLException {
        database = config.getString("auth.mysql.database");
        host = config.getString("auth.mysql.host");
        port = config.getString("auth.mysql.port");
        username = config.getString("auth.mysql.username");
        password = config.getString("auth.mysql.password");
        tableName = config.getString("auth.mysql.tableName");
        columnName = config.getString("auth.mysql.columnName");
        columnPassword = config.getString("auth.mysql.columnPassword");
        columnIp = config.getString("auth.mysql.columnIp");
        columnLastLogin = config.getString("auth.mysql.columnLastLogin");

        connect();
    }

    private synchronized void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        KCLogger.info("MySQL driver loaded");

        try {
            connection = DriverManager.getConnection(getConnString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        KCLogger.info("Connection ready");
    }

    private synchronized void reconnect() {
        try {
            connection = DriverManager.getConnection(getConnString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getConnString() {
        return "jdbc:mysql://" + host + ":" + port
                + "/" + database + "?zeroDateTimeBehavior=convertToNull";
    }

    public void getAuth(KPlayer kPlayer) {
        String user = kPlayer.getPlayer().getName().toLowerCase();

        Connection con;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = getConnection();

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
                if (rs.getString(columnIp) == null || rs.getString(columnIp).isEmpty()) {
                    kPlayer.setLoginIp("198.18.0.1");
                } else {
                    kPlayer.setLoginIp(rs.getString(columnIp));
                }
                kPlayer.setPassword(rs.getString(columnPassword));
                kPlayer.setLoginDate(rs.getLong(columnLastLogin));
                kPlayer.setSalt(rs.getString("salt"));
                kPlayer.setGroupId(rs.getInt("groupid"));
            }
        } catch (SQLException ex) {
            KCLogger.showError(ex.getMessage());
        } finally {
            close(rs);
            close(pst);
        }

    }

    public boolean updateSession(KPlayer kPlayer) {
        Connection con;
        PreparedStatement pst = null;
        try {
            con = getConnection();
            pst = con.prepareStatement("UPDATE " + tableName + " SET " + columnIp
                    + "=?, " + columnLastLogin + "=? WHERE " + columnName + "=?;");
            pst.setString(1, kPlayer.getPlayer().getAddress().getHostString());
            pst.setLong(2, System.currentTimeMillis());
            pst.setString(3, kPlayer.getNameLowCase());
            pst.executeUpdate();
        } catch (SQLException ex) {
            KCLogger.showError(ex.getMessage());
            return false;
        } finally {
            close(pst);
        }
        return true;
    }

    private synchronized Connection getConnection() {
        try {
            if (!connection.isValid(4)) {
                reconnect();
            }
        } catch (SQLException e) {
            reconnect();
        }
        return connection;
    }

    private void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ex) {
                KCLogger.showError(ex.getMessage());
            }
        }
    }

    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                KCLogger.showError(ex.getMessage());
            }
        }
    }
}
