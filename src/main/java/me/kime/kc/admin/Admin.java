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
package me.kime.kc.admin;

import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Kime
 */
public class Admin extends Addon {
    private final AdminLinstener adminListener;

    public Admin(KimeCraft instance) {
        super(instance);
        adminListener = new AdminLinstener(this);
    }

    @Override
    public String getAddonName() {
        return "Admin";
    }

    @Override
    public void onEnable() {
        plugin.getPluginManager().registerEvents(adminListener, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(adminListener);
    }
}
