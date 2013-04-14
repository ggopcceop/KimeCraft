package me.kime.kc.Lander;

import java.util.LinkedList;

public class KLand {

    private int id;
    private String owner;
    private String name;
    private LinkedList<KChunk> chunks;

    public KLand(int id, String owner, String name) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        chunks = new LinkedList<>();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<KChunk> getChunks() {
        return chunks;
    }

    public void addChunk(KChunk chunk) {
        this.chunks.addLast(chunk);
    }
}
