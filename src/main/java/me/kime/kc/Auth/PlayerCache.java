package me.Kime.KC.Auth;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Cache player status when player login
 *
 * @author Kime
 *
 */
public class PlayerCache {

    private int foodLevel;
    private float exp;
    private int level;
    private int health;
    //private int gameMode;
    private ItemStack[] contents;
    private ItemStack[] armor;
    private int air;

    public void cacheInventory(PlayerInventory inventory) {
        contents = inventory.getContents();
        armor = inventory.getArmorContents();
        inventory.clear();
    }

    public void restoreInventory(PlayerInventory inventory) {
        inventory.setContents(contents);
        inventory.setArmorContents(armor);
        contents = null;
        armor = null;
    }

    public void cacheStatus(Player player) {
        foodLevel = player.getFoodLevel();
        exp = player.getExp();
        level = player.getLevel();

        health = player.getHealth();
        if (health < 0 || health > 20) {
            health = 0;
        }

        air = player.getRemainingAir();

        player.setExp(0);
        if (health != 0) {
            player.setHealth(20);
        } else {
            health = -1;
        }

        player.setRemainingAir(20);
    }

    public void restoreStatus(Player player) {
        player.setFoodLevel(foodLevel);
        player.setExp(exp);
        player.setLevel(level);

        if (health != -1) {
            player.setHealth(health);
        }

        //player.setGameMode(GameMode.getByValue(gameMode));
        if (air < 0) {
            player.setRemainingAir(0);
        } else {
            player.setRemainingAir(air);
        }
    }
}
