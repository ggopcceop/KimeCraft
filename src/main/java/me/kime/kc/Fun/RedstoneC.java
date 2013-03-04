package me.Kime.KC.Fun;

public class RedstoneC {
	private int count;
	private long date;
	
	public RedstoneC(){
		count = 0;
		date = 0L;
	}
	
	public synchronized void add(){
		count++;
	}
	
	public synchronized boolean isOver(long d){
		if(count>15){
			if(((d - date)/1000) > 8){
				date = d;
				count = 0;
				return false;
			}
			//System.out.print(((d - date)/1000) + " " + count);
			return true;
		}
		return false;
	}
	
	public long getTime(){
		return date;
	}
	
	public synchronized void setDate(long d){
		date = d;
	}

}
