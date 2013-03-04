package me.Kime.KC.Auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.Kime.KC.KPlayer;
import me.Kime.KC.Util.KCLogger;

/**
 * SQL connecter to Auth
 * 
 * @author Kime
 *
 */
public class DataSource {
	private final String database = "e7playbbssql";
	private final String host = "e7playbbssql.db.7835405.hostedresource.com";
	private final String port = "3306";
	private final String username = "e7playbbssql";
	private final String password = "Kime-125456";
	private final String tableName = "pre_ucenter_members";
	private final String columnName = "username";
	private final String columnPassword = "password";
    private final String columnIp = "lloginip";
    private final String columnLastLogin = "llogindate";
    
	private Connection connection;
	
	public DataSource() throws ClassNotFoundException, SQLException{
		connect();
	}
	
    private synchronized void connect(){
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
    
    private synchronized void reconnect(){
    	try {
			connection = DriverManager.getConnection(getConnString(), username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

	private String getConnString(){
		return "jdbc:mysql://" + host + ":" + port
				+ "/" + database + "?zeroDateTimeBehavior=convertToNull";
    }

    public void getAuth(KPlayer kPlayer) {
    	String user = kPlayer.getPlayer().getName().toLowerCase();
    	
    	Connection con = null;
    	PreparedStatement pst = null;
    	ResultSet rs = null, rs2 = null;
    	try {
    		con = getConnection();
    		
    		pst = con.prepareStatement("SELECT * FROM " + tableName + " WHERE " + columnName + "=?;");
    		pst.setString(1, user);
    		rs = pst.executeQuery();

    		pst = con.prepareStatement("SELECT * FROM pre_common_member WHERE username=?;");
    		pst.setString(1, user);
    		rs2 = pst.executeQuery();

    		if (rs.next() && rs2.next()) {
    			if (rs.getString(columnIp) == null || rs.getString(columnIp).isEmpty()) {
    				kPlayer.setLoginIp("198.18.0.1");
    			} else {
    				kPlayer.setLoginIp(rs.getString(columnIp));
    			}
    			kPlayer.setPassword(rs.getString(columnPassword));
    			kPlayer.setLoginDate(rs.getLong(columnLastLogin));
    			kPlayer.setSalt(rs.getString("salt"));
    			kPlayer.setGroupId(rs2.getInt("groupid"));
    		}
    	} catch (SQLException ex) {
    		KCLogger.showError(ex.getMessage());
    	} finally {
    		close(rs);
    		close(rs2);
    		close(pst);
    	}

    }
    
	public boolean updateSession(KPlayer kPlayer) {
		Connection con = null;
        PreparedStatement pst = null;
        try {
            con = getConnection();
            pst = con.prepareStatement("UPDATE " + tableName + " SET " + columnIp + "=?, " + columnLastLogin + "=? WHERE " + columnName + "=?;");
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
	
	private synchronized Connection getConnection(){
		try {
			if(!connection.isValid(4)){
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
