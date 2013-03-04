package me.Kime.KC.Task.ThreadTask;

import java.util.LinkedList;

import me.Kime.KC.KPlayer;
import me.Kime.KC.Auth.Auth;

public class SessionSQLTask extends TTask {
	
	private Auth plugin;
	private LinkedList<KPlayer> list;

	public SessionSQLTask(Auth auth) {
		this.list = new LinkedList<KPlayer>();
		this.plugin = auth;
	}

	public void run() {
		KPlayer kPlayer = null;

		synchronized(list){
			if(!list.isEmpty()){
				kPlayer = list.removeFirst();
			}
			else{
				return;
			}
		}
		plugin.getDataSource().updateSession(kPlayer);		
	}
	
	public void queue(KPlayer kPlayer){
		list.addLast(kPlayer);
	}
	
	@Override
	public int queueSize() {
		return list.size();
	}


}
