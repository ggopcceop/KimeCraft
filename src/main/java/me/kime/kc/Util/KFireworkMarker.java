package me.kime.kc.Util;

import java.util.Random;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 *
 * @author Kime
 */
public class KFireworkMarker {

    /**
     * Play a pretty firework at the location with the FireworkEffect when
     * called
     *
     * @param world
     * @param loc
     * @param fe
     */
    public static void playFirework(World world, Location loc, FireworkEffect fe, int power) {
        Firework fw = (Firework) world.spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta data = (FireworkMeta) fw.getFireworkMeta();
        data.setPower(power);
        data.addEffect(fe);
        fw.setFireworkMeta(data);
    }

    public static void playRandomFirework(World world, Location loc, Random random) {
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);

        int typeid = random.nextInt(5);
        Type type;
        switch (typeid) {
            case 0:
                type = Type.BALL;
                break;
            case 1:
                type = Type.BALL_LARGE;
                break;
            case 2:
                type = Type.BURST;
                break;
            case 3:
                type = Type.CREEPER;
                break;
            default:
                type = Type.STAR;
        }

        int power = random.nextInt(4) + 2;
        FireworkEffect fe = FireworkEffect.builder()
                .withColor(Color.fromRGB(r, g, b)).with(type)
                .trail(random.nextBoolean()).flicker(random.nextBoolean())
                .build();

        playFirework(world, loc, fe, power);
    }
}
