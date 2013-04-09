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
    private final Byte lock;

    public EntityFunTask() {
        this.lock = Byte.MAX_VALUE;
        list = new LinkedList<>();
        rnd = new Random();
    }

    @Override
    public void run() {
        Entity entity;

        synchronized (lock) {
            if (!list.isEmpty()) {
                entity = list.removeFirst();
            } else {
                return;
            }
        }

        if (entity instanceof Sheep) {
            Sheep sheep = (Sheep) entity;
            sheep.setColor(DyeColor.values()[rnd.nextInt(DyeColor.values().length)]);
        }
    }

    public void queue(Entity entity) {
        list.addLast(entity);
    }

    @Override
    public int queueSize() {
        return list.size();
    }
}
