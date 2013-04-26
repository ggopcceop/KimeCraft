package me.kime.kc.SignTP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.kime.kc.Database.DataSource;
import me.kime.kc.Util.KCLogger;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author Kime
 */
public class SignTpDataSource extends DataSource {

    private final SignTP signtp;

    public SignTpDataSource(String host, String user, String pass, String db
            , int maxConnections, SignTP signtp) {
        super(host, user, pass, db, maxConnections);
        this.signtp = signtp;
    }

    public Location getLocationByName(String name) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = pool.getValidConnection();
            pst = conn.prepareStatement("SELECT * FROM kc_signtp WHERE name = ?");
            pst.setString(1, name);
            rs = pst.executeQuery();
            if (rs.next()) {
                String worldname = rs.getString("world");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                double yaw = rs.getDouble("yaw");
                World world = signtp.getPlugin().getServer().getWorld(worldname);
                return new Location(world, x, y, z, (float) yaw, 0);
            }
            return null;
        } catch (SQLException e) {
            KCLogger.showError(e.getMessage());
            return null;
        } finally {
            close(conn);
            close(pst);
            close(rs);
        }
    }

    public boolean insertLocation(Location loc, String name) {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = pool.getValidConnection();
            pst = conn.prepareStatement("INSERT INTO kc_signtp values(DEFAULT, ?, ?, ?, ?, ?, ?)");
            pst.setString(1, name.toLowerCase());
            pst.setString(2, loc.getWorld().getName());
            pst.setDouble(3, loc.getX());
            pst.setDouble(4, loc.getY());
            pst.setDouble(5, loc.getZ());
            pst.setDouble(6, loc.getYaw() + 180F);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            KCLogger.showError(e.getMessage());
            return false;
        } finally {
            close(conn);
            close(pst);
        }
    }

    public void removeLocation(String name) {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = pool.getValidConnection();
            pst = conn.prepareStatement("DELETE FROM kc_signtp WHERE name = ?");
            pst.setString(1, name.toLowerCase());
            pst.executeUpdate();
        } catch (SQLException e) {
            KCLogger.showError(e.getMessage());
        } finally {
            close(conn);
            close(pst);
        }
    }
}
