package me.Kime.KC.Task.ThreadTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import me.Kime.KC.Fun.Fun;
import me.Kime.KC.Fun.RedstoneC;

import org.bukkit.Location;

public class RedstoneCounterCleanTask extends TTask {

	private Fun fun;
	private int count = 0;
	private Byte lock = Byte.MAX_VALUE;

	public RedstoneCounterCleanTask(Fun fun) {
		this.fun = fun;
	}

		public void run() {
		count = 0;
		synchronized(lock){
			HashMap<Location, RedstoneC> list = fun.getRedstone();
		    Iterator<Location> locs = list.keySet().iterator();
		    LinkedList<Location> removeList = new LinkedList<Location>();
		    long time = System.currentTimeMillis();
		    
		    while(locs.hasNext()){
		    	Location loc = locs.next();
		    	if(loc.getBlock().getTypeId() != 55){
		    		removeList.add(loc);
		    	}
		    	else{
		    		RedstoneC c = list.get(loc);
		    		if((time - c.getTime()) > 120000){
		    			removeList.add(loc);
		    		}
		    	}
		    }
		    while(!removeList.isEmpty()){
		    	Location loc = removeList.removeFirst();
		    	list.remove(loc);
		    }	    
		}	   
	}
	
	public void queue(){
		count++;
	}
	
	@Override
	public int queueSize() {
		return count;
	}

}
