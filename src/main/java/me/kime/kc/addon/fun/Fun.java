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
package me.kime.kc.addon.fun;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import me.kime.kc.task.async.Caller;
import me.kime.kc.task.async.async;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;

public class Fun extends Addon {

    private final HashMap<Location, RedstoneC> redstone = new HashMap<>();
    public final int R = 300;
    public final int RR = R * R;
    public final int sRR = (R - 2) * (R - 2);
    private int repeatingTaskID;
    private final FunCommand funCommand;
    private CommandExecutor defaultSkullExecutor;
    private CommandExecutor defaultRollExecutor;
    private final FunLinstener funListener;

    public Fun(KimeCraft instance) {
        super(instance);
        funCommand = new FunCommand(this);

        funListener = new FunLinstener(this);

    }

    @Override
    public String getAddonName() {
        return "Fun";
    }

    @Override
    public void onEnable() {

        Caller caller = async.call().call(t -> {
            HashMap<Location, RedstoneC> list = getRedstone();
            Iterator<Location> locs = list.keySet().iterator();
            LinkedList<Location> removeList = new LinkedList<>();
            long time = System.currentTimeMillis();

            while (locs.hasNext()) {
                Location loc = locs.next();
                RedstoneC c = list.get(loc);
                if ((time - c.getTime()) > 120000) {
                    removeList.add(loc);
                }
            }
            while (!removeList.isEmpty()) {
                Location loc = removeList.removeFirst();
                list.remove(loc);
            }
        }).caller();

        repeatingTaskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, caller::call, 305L, 6001L);

        KCraftingRecipe kCraftingRecipe = new KCraftingRecipe(this);

        defaultSkullExecutor = plugin.getCommand("skull").getExecutor();
        defaultRollExecutor = plugin.getCommand("roll").getExecutor();

        plugin.getCommand("skull").setExecutor(funCommand);
        plugin.getCommand("roll").setExecutor(funCommand);

        plugin.getPluginManager().registerEvents(funListener, plugin);
    }

    @Override
    public void onDisable() {
        plugin.getServer().getScheduler().cancelTask(repeatingTaskID);

        plugin.getCommand("skull").setExecutor(defaultSkullExecutor);
        plugin.getCommand("roll").setExecutor(defaultRollExecutor);

        HandlerList.unregisterAll(funListener);
    }

    public HashMap<Location, RedstoneC> getRedstone() {
        return redstone;
    }

}
