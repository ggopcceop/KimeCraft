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
package me.kime.kc.addon.motd;

import java.util.HashMap;
import java.util.Set;
import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import me.kime.kc.locale.Locale;
import me.kime.kc.task.MOTDBoradcastTask;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Kime
 */
public class MOTD extends Addon {

    public HashMap<String, Locale> motd;
    public HashMap<String, Locale> rules;
    private BukkitTask boradcastTaskID;
    private final MOTDListener motdListener;

    public MOTD(KimeCraft plugin) {
        super(plugin);
        motdListener = new MOTDListener(this);
    }

    @Override
    public String getAddonName() {
        return "MOTD";
    }

    @Override
    public void onEnable() {
        motd = getLocales(plugin.config.getConfigurationSection("motd.motd"));
        rules = getLocales(plugin.config.getConfigurationSection("motd.rules"));

        int boardcastInterval = plugin.config.getInt("motd.boardcastInterval", 300) * 20;

        boradcastTaskID = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new MOTDBoradcastTask(this), 0, boardcastInterval);

        plugin.getPluginManager().registerEvents(motdListener, plugin);
    }

    @Override
    public void onReload() {
        onDisable();
        onEnable();
    }

    @Override
    public void onDisable() {
        plugin.getServer().getScheduler().cancelTask(boradcastTaskID.getTaskId());
        HandlerList.unregisterAll(motdListener);
    }

    private HashMap<String, Locale> getLocales(ConfigurationSection config) {
        HashMap<String, Locale> result = new HashMap<>();
        Set<String> keys = config.getKeys(false);
        keys.stream().forEach((key) -> {
            result.put(key, new Locale(config.getConfigurationSection(key), key));
        });
        return result;
    }
}
