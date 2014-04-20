/*
 * Copyright (C) 2014 Kime
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.kime.kc.addon.auth;

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
    private double health;
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
            player.setHealth(20.0);
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
