package me.kime.kc.util;

import org.bukkit.Color;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 *
 * @author Kime
 */
public class KCItemManger {

    public static void addItemToInventory(String[] split, Inventory chest) {
        if (split.length < 1) {
            return;
        }
        int itemid = Integer.parseInt(split[0]);
        int amount;

        if (split.length == 1) {
            amount = 1;
        } else {
            amount = Integer.parseInt(split[1]);
        }
        byte data = 0;
        int rgb = 0;
        if (split.length == 3) {
            data = Byte.parseByte(split[2]);
        } else if (split.length == 5) {
            rgb += (Integer.parseInt(split[2], 16) << 16);
            rgb += (Integer.parseInt(split[3], 16) << 8);
            rgb += Integer.parseInt(split[4], 16);
        }
        ItemStack item = new ItemStack(itemid);
        int maxStack = item.getMaxStackSize();
        int count = 10;
        while (amount > maxStack) {
            if (count < 0) {
                if (amount > maxStack) {
                    amount = maxStack;
                }
                break;
            }
            item = new ItemStack(Integer.parseInt(split[0]), maxStack);
            if (split.length == 3) {
                item.getData().setData(data);
            } else if (split.length == 5) {
                if (item.getItemMeta() instanceof LeatherArmorMeta) {
                    LeatherArmorMeta armor = (LeatherArmorMeta) item.getItemMeta();
                    armor.setColor(Color.fromRGB(rgb));
                    item.setItemMeta(armor);
                }
            }
            chest.addItem(item);
            amount -= maxStack;
            count--;
        }
        item = new ItemStack(Integer.parseInt(split[0]), amount);
        if (split.length == 3) {
            item.getData().setData(data);
        } else if (split.length == 5) {
            if (item.getItemMeta() instanceof LeatherArmorMeta) {
                LeatherArmorMeta armor = (LeatherArmorMeta) item.getItemMeta();
                armor.setColor(Color.fromRGB(rgb));
                item.setItemMeta(armor);
            }
        }

        chest.addItem(item);
    }
}
