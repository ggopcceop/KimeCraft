package me.Kime.KC.Task;

import me.Kime.KC.KPlayer;
import me.Kime.KC.Auth.Auth;

/**
 * Kick non-auth player when time out
 * 
 * @author Kime
 *
 */
public class AuthTimeoutTask implements Runnable{
	
	private String name;
	private Auth plugin;

	public AuthTimeoutTask(Auth instance, String name){
		this.plugin = instance;
		this.name = name;
	}
	
	@Override
	public void run() {
		KPlayer kPlayer = plugin.getOnlinePlayer(name);
		if(kPlayer != null){
			kPlayer.setTimeoutTaskId(-1);
			if(!kPlayer.isAuth()){
				kPlayer.getPlayer().kickPlayer("Timeout");
			}
		}
		//debug
		//System.out.println(name + "'s timeout task suicided");
	}
}
