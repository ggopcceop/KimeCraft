package me.kime.kc.Fun;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DeathCache {

    private ItemStack[] armor;
    private ItemStack[] item;

    public void cache(PlayerInventory inventory) {
        item = inventory.getContents();
        armor = inventory.getArmorContents();
    }

    public void restore(PlayerInventory inventory) {
        inventory.setContents(item);
        inventory.setArmorContents(armor);
    }

    public void dropCache(Location location) {
        World world = location.getWorld();
        for (int i = 0; i < item.length; i++) {
            world.dropItemNaturally(location, item[i]);
        }
        for (int i = 0; i < armor.length; i++) {
            world.dropItemNaturally(location, armor[i]);
        }
    }
}
