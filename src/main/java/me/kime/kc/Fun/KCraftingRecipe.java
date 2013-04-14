package me.kime.kc.Fun;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class KCraftingRecipe {

    private Fun fun;

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
