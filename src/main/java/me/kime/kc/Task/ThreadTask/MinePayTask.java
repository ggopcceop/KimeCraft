package me.Kime.KC.Task.ThreadTask;

import java.util.LinkedList;

import me.Kime.KC.KPlayer;
import me.Kime.KC.Mine.Mine;

public class MinePayTask extends TTask {

	private LinkedList<MineData> list;
	private Mine mine;
	private final double payPerItem;
	
	public MinePayTask(Mine mine, double payPerItem) {
		this.mine = mine;
		this.payPerItem = payPerItem;
		
		list = new LinkedList<MineData>();
	}

	public void run() {
		MineData data = null;
		
		synchronized(list){
			if(!list.isEmpty()){
				data = list.removeFirst();
			}
			else{
				return;
			}
		}
		
		KPlayer player = mine.getPlugin().getOnlinePlayer(data.name);
		
		if(player.getLastMine() == data.id){
			player.setPayRate(player.getPayRate() * 0.9);
		}
		else{
			player.setPayRate(payPerItem);
		}
		
		if(player.getPayRate() < 0.01) player.setPayRate(0);
		
		player.setLastMine(data.id);
		player.addSalary(player.getPayRate());		

	}

	@Override
	public int queueSize() {
		return list.size();
	}

	public void queue(int id, String name) {
		list.addLast(new MineData(id, name));
	}
	
	
	private class MineData{
		public MineData(int id, String name) {
			this.id = id;
			this.name = name;
		}
		public int id;
		public String name;
	}

}
