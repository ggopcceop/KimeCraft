package me.Kime.KC.Task.ThreadTask;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Spider;

public class EntityFunTask extends TTask {
	
	private LinkedList<Entity> list;
	private Random rnd;
	
	public EntityFunTask(){
		list = new LinkedList<Entity>();
		rnd = new Random();
	}
	
		public void run() {
		Entity entity = null;

		synchronized(list){
			if(!list.isEmpty()){
				entity = list.removeFirst();
			}
			else{
				return;
			}
		}
		
		if(entity instanceof Sheep){
			Sheep sheep = (Sheep) entity;
			sheep.setColor(DyeColor.values()[rnd.nextInt(DyeColor.values().length)]);
		}
		else if(entity instanceof Spider){
			Spider spider = (Spider) entity;
			Entity skeleton = spider.getWorld().spawnEntity(spider.getLocation(), EntityType.SKELETON);
			spider.setPassenger(skeleton);
		}
	}
	
	public void queue(Entity entity){
		list.addLast(entity);
	}
	
	public int queueSize(){
		return list.size();
	}
}
