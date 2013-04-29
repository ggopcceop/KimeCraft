package me.kime.kc.ChopTree;

import me.kime.kc.KimeCraft;

public class ChopTree {

    private KimeCraft plugin;
    private String[] allowTools = {"WOOD_AXE", "STONE_AXE", "IRON_AXE", "GOLD_AXE", "DIAMOND_AXE"};
    private int leafRadius = 1;
    private boolean isPopLeaves = true;
    private boolean isInterruptIfToolBreaks = true;
    private boolean isMoreDamageToTools = true;

    public ChopTree(KimeCraft kc) {
        this.plugin = kc;

        plugin.getPluginManager().registerEvents(new ChopTreeBlockListener(this), plugin);
    }

    public String[] getAllowedTools() {
        return allowTools;
    }

    public int getLeafRadius() {
        return leafRadius;
    }

    public boolean isPopLeaves() {
        return isPopLeaves;
    }

    public boolean isInterruptIfToolBreaks() {
        return isInterruptIfToolBreaks;
    }

    public boolean isMoreDamageToTools() {
        return isMoreDamageToTools;
    }
}
