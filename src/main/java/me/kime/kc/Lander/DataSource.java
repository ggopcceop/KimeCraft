package me.Kime.KC.Lander;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.Kime.KC.Util.KCLogger;

/**
 * SQL connecter to Auth
 *
 * @author Kime
 *
 */
public class DataSource {

    private final String database = "minecraft";
    private final String host = "mc.kime.co";
    private final String port = "3306";
    private final String username = "mcuser";
    private final String password = "125456";
    @SuppressWarnings("unused")
    private final String tableName = "lander";
    private ConnectionPool connectionPool;

    public DataSource() throws ClassNotFoundException, SQLException {
        connectionPool = new ConnectionPool(getConnString(), username, password, 10);
    }

    private String getConnString() {
        return "jdbc:mysql://" + host + ":" + port
                + "/" + database + "?zeroDateTimeBehavior=convertToNull";
    }

    public int getId(int x, int z) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        int id = -1;
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            pst = conn.prepareStatement("SELECT * FROM `lander_cords` WHERE `x` = ? AND `z` = ?;");
            pst.setInt(1, x);
            pst.setInt(2, z);
            rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt("land");
            }
        } catch (SQLException e) {
            KCLogger.showError("Lander DataSource getOnwerError: " + e.getMessage());
        } finally {
            close(rs);
            close(pst);
            connectionPool.release(conn);
        }
        return id;

    }

    public void setOwner(int x, int z, String owner) {
        PreparedStatement pst = null;
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            pst = conn.prepareStatement("INSERT INTO `lander`(`x`, `z`, `owner`) VALUES (?, ?, ?);");
            pst.setInt(1, x);
            pst.setInt(2, z);
            pst.setString(3, owner.toLowerCase());
            pst.executeUpdate();
        } catch (SQLException e) {
            KCLogger.showError("Lander DataSource setOnwerError: " + e.getMessage());
        } finally {
            close(pst);
            connectionPool.release(conn);
        }
    }

    public KLand getLand(int id) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        KLand kLand = null;
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            pst = conn.prepareStatement("SELECT * FROM `lander_land` WHERE `id` = ?;");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                String owner = rs.getString("owner");
                String name = rs.getString("name");
                kLand = new KLand(id, owner, name);
            }
        } catch (SQLException e) {
            KCLogger.showError("Lander DataSource getLandError: " + e.getMessage());
        } finally {
            close(rs);
            close(pst);
            connectionPool.release(conn);
        }
        return kLand;
    }

    public void addChunk(int x, int z, int id) {
        PreparedStatement pst = null;
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            pst = conn.prepareStatement("INSERT INTO `lander_cords`(`x`, `z`, `land`) VALUES (?, ?, ?);");
            pst.setInt(1, x);
            pst.setInt(2, z);
            pst.setInt(3, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            KCLogger.showError("Lander DataSource addChunkError: " + e.getMessage());
        } finally {
            close(pst);
            connectionPool.release(conn);
        }
    }

    public int addLand(String owner, String name) {
        PreparedStatement pst = null;
        Connection conn = null;
        ResultSet rs = null;
        int id = -1;
        try {
            conn = connectionPool.getConnection();
            pst = conn.prepareStatement("INSERT INTO `lander_land`(`owner`, `name`) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, owner.toLowerCase());
            pst.setString(2, name);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            KCLogger.showError("Lander DataSource addLandError: " + e.getMessage());
        } finally {
            close(pst);
            close(rs);
            connectionPool.release(conn);
        }
        return id;
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
