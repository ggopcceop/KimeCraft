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
package me.kime.kc.signTP;

import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import org.bukkit.event.HandlerList;

public class SignTP extends Addon {

    private SignTpDataSource datasource;
    private String databaseKey;
    private final SignTPListener signTPListener;

    public SignTP(KimeCraft plugin) {
        super(plugin);
        signTPListener = new SignTPListener(this);
    }

    @Override
    public String getAddonName() {
        return "SignTP";
    }

    @Override
    public void onEnable() {
        loadConfig();

        datasource = new SignTpDataSource(databaseKey, this);

        datasource.initTable();

        plugin.getServer().getPluginManager().registerEvents(signTPListener, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(signTPListener);
    }

    @Override
    public void onReload() {
        loadConfig();
    }

    private void loadConfig() {
        databaseKey = plugin.getConfig().getString("signtp.mysql", "minecraft");
    }

    public SignTpDataSource getDataSource() {
        return datasource;
    }

}
