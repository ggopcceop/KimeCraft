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
package me.kime.kc.addon.chopTree;

import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import org.bukkit.event.HandlerList;

public class ChopTree extends Addon {

    private final String[] allowTools = {"WOOD_AXE", "STONE_AXE", "IRON_AXE", "GOLD_AXE", "DIAMOND_AXE"};
    private final int leafRadius = 1;
    private final boolean isPopLeaves = true;
    private final boolean isInterruptIfToolBreaks = true;
    private final boolean isMoreDamageToTools = true;
    private final ChopTreeBlockListener chopTreeBlockListener;

    public ChopTree(KimeCraft kc) {
        super(kc);
        chopTreeBlockListener = new ChopTreeBlockListener(this);
    }

    @Override
    public String getAddonName() {
        return "ChopTree";
    }

    @Override
    public void onEnable() {
        plugin.getPluginManager().registerEvents(chopTreeBlockListener, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(chopTreeBlockListener);
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
