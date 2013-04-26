package me.kime.kc.Task.ThreadTask;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;

public class EntityFunTask extends Task {

    private LinkedList<Entity> list;
    private Random rnd;

    public EntityFunTask() {
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
