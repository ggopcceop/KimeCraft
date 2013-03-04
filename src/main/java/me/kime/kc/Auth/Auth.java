package me.Kime.KC.Auth;

import java.sql.SQLException;

import me.Kime.KC.KC;
import me.Kime.KC.KPlayer;

/**
 * Auth plugin
 * 
 * @author Kime
 *
 */
public class Auth {
	private final long sessionTime = 1000 * 120;
	
	private KC plugin;
	private DataSource dataSource = null;

	public Auth(KC instance){
		this.plugin = instance;
		
		//start sql connection
		try {
			dataSource = new DataSource();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		//login command executor
		plugin.getCommand("login").setExecutor(new AuthCommand(this));
		
		//login event
		plugin.getPluginManager().registerEvents(new AuthLinstener(this), plugin);
	}
	
	public KPlayer getOnlinePlayer(String name){
		return plugin.getOnlinePlayer(name);
	}
	
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public KC getPlugin(){
		return plugin;
	}

	public long getSessionTime() {
		return sessionTime;
	}
}
