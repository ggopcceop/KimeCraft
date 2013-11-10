package me.kime.kc.Mine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.kime.kc.Database.DataSource;
import me.kime.kc.Util.KCLogger;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Kime
 */
public class MineDataSource extends DataSource {
    private final Mine mine;

    public MineDataSource(String host, String user, String pass, String db, int max, Mine plugin) {
        super(host, user, pass, db, max);
        mine = plugin;
    }

    public void logPlayerLocation(Player player, Location loc) {
        String user = player.getName().toLowerCase();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = pool.getValidConnection();
            pst = con.prepareStatement("INSERT INTO kc_mine_player (name, world, x, y, z)"
                    + " VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name=values(name),"
                    + " world=VALUES(world), x=VALUES(x), y=VALUES(y), z=VALUES(z);");
            pst.setString(1, user);
            pst.setString(2, loc.getWorld().getName());
            pst.setDouble(3, loc.getX());
            pst.setDouble(4, loc.getY());
            pst.setDouble(5, loc.getZ());
            pst.executeUpdate();

        } catch (SQLException ex) {
            KCLogger.showError(ex.getMessage());
        } finally {
            close(con);
            close(rs);
            close(pst);
        }
    }

    public Location checkLogedPlayerLocation(Player player) {
        String user = player.getName().toLowerCase();
        
        Location newLoc = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = pool.getValidConnection();
            pst = con.prepareStatement("SELECT * FROM kc_mine_player WHERE name=?;");
            pst.setString(1, user);
            rs = pst.executeQuery();

            if (rs.next()) {
                World world = mine.getPlugin().getServer().getWorld(rs.getString("world"));
                 newLoc = new Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
            }

        } catch (SQLException ex) {
            KCLogger.showError(ex.getMessage());
        } finally {
            close(con);
            close(rs);
            close(pst);
        }
        
        return newLoc;
    }
}
