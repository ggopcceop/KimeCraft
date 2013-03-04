package me.Kime.KC.Lander;

import org.bukkit.Chunk;

public class KChunk {
	private int id;
	private Chunk chunk;
	private String owner;
	
	public KChunk(Chunk chunk){
		id = -1;
		this.chunk = chunk;
	}
	
	public Chunk getChunk(){
		return chunk;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
