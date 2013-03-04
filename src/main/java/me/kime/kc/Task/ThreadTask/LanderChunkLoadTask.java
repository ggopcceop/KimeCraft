package me.Kime.KC.Task.ThreadTask;

import java.util.LinkedList;

import me.Kime.KC.Lander.KChunk;
import me.Kime.KC.Lander.KLand;
import me.Kime.KC.Lander.Lander;
import org.bukkit.Chunk;

public class LanderChunkLoadTask extends TTask {

	private LinkedList<Chunk> list;
	private Lander lander;
	
	public LanderChunkLoadTask(Lander lander){
		this.lander = lander;
		list = new LinkedList<Chunk>();
	}
		public void run() {
		Chunk chunk = null;

		synchronized(list){
			if(!list.isEmpty()){
				chunk = list.removeFirst();
			}
			else{
				return;
			}
		}

		if(lander.getKChunk(chunk.getX(), chunk.getZ()) == null){
			int id = lander.getDataSource().getId(chunk.getX(), chunk.getZ());

			KChunk kChunk = new KChunk(chunk);
			lander.loadChunk(chunk.getX(), chunk.getZ(), kChunk);

			if(id != -1){
				kChunk.setId(id);
				KLand kLand = lander.getLand(id);
				if(kLand == null){
					kLand = lander.getDataSource().getLand(id);
					lander.loadLand(id, kLand.getOwner(), kLand.getName()
							, chunk.getX(), chunk.getZ());
				}
				kChunk.setOwner(kLand.getOwner());
			}
		}			

	}

	@Override
	public int queueSize() {
		return list.size();
	}
	
	public void queue(Chunk chunk) {
		list.addLast(chunk);		
	}

}
