package me.kime.kc.Auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.kime.kc.Database.DataSource;

import me.kime.kc.KPlayer;
import me.kime.kc.Util.KCLogger;

/**
 * SQL connecter to Auth
 *
 * @author Kime
 *
 */
public class AuthDataSource extends DataSource {

    public AuthDataSource(String host, String user, String pass, String db, int max) {
        super(host, user, pass, db, max);
    }

    public void getAuth(KPlayer kPlayer) {
        String user = kPlayer.getPlayer().getName().toLowerCase();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = pool.getValidConnection();
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
                if (rs.getString("lloginip") == null || rs.getString("lloginip").isEmpty()) {
                    kPlayer.setLoginIp("198.18.0.1");
                } else {
                    kPlayer.setLoginIp(rs.getString("lloginip"));
                }
                kPlayer.setPassword(rs.getString("password"));
                kPlayer.setLoginDate(rs.getLong("llogindate"));
                kPlayer.setSalt(rs.getString("salt"));
                kPlayer.setGroupId(rs.getInt("groupid"));
            }
        } catch (SQLException ex) {
            KCLogger.showError(ex.getMessage());
        } finally {
            close(con);
            close(rs);
            close(pst);
        }

    }

    public boolean updateSession(KPlayer kPlayer) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = pool.getValidConnection();
            pst = con.prepareStatement("UPDATE pre_ucenter_members SET lloginip = ?,"
                    + " llogindate = ? WHERE username = ?;");
            pst.setString(1, kPlayer.getPlayer().getAddress().getHostString());
            pst.setLong(2, System.currentTimeMillis());
            pst.setString(3, kPlayer.getNameLowCase());
            pst.executeUpdate();
        } catch (SQLException e) {
            KCLogger.showError(e.getMessage());
            return false;
        } finally {
            close(con);
            close(pst);
        }
        return true;
    }
}
