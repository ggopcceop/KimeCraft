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
package me.kime.kc.fun;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class KCraftingRecipe {

    private final Fun fun;

    public KCraftingRecipe(Fun fun) {
        this.fun = fun;
        addRecipes();
    }

    private void addRecipes() {
        Server server = fun.getPlugin().getServer();

        ShapedRecipe halfDoor = new ShapedRecipe(new ItemStack(Material.WOODEN_DOOR, 2));
        halfDoor.shape("DA", "AD");
        halfDoor.setIngredient('D', Material.WOOD_DOOR);
        halfDoor.setIngredient('A', Material.DIAMOND_AXE);

        server.addRecipe(halfDoor);

        ShapedRecipe buringFurnace = new ShapedRecipe(new ItemStack(Material.BURNING_FURNACE));
        buringFurnace.shape("SSS", " F ", "CCC");
        buringFurnace.setIngredient('S', Material.FLINT_AND_STEEL);
        buringFurnace.setIngredient('F', Material.FURNACE);
        buringFurnace.setIngredient('C', Material.COAL);

        server.addRecipe(buringFurnace);

        ShapedRecipe clayBall = new ShapedRecipe(new ItemStack(Material.CLAY_BALL, 2));
        clayBall.shape("SG", "GS");
        clayBall.setIngredient('S', Material.SAND);
        clayBall.setIngredient('G', Material.GRAVEL);

        server.addRecipe(clayBall);

        ShapedRecipe snow = new ShapedRecipe(new ItemStack(Material.SNOW, 3));
        snow.shape("SSS");
        snow.setIngredient('S', Material.SNOW_BALL);

        server.addRecipe(snow);

        ShapedRecipe bookshelf = new ShapedRecipe(new ItemStack(Material.BOOKSHELF));
        bookshelf.shape("WWW", "PBP", "WWW");
        bookshelf.setIngredient('W', Material.WOOD);
        bookshelf.setIngredient('B', Material.BOOK);
        bookshelf.setIngredient('P', Material.PAPER);

        server.addRecipe(bookshelf);
    }
}
