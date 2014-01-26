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

public class SignTP extends Addon {

    private SignTpDataSource datasource;

    public SignTP(KimeCraft plugin) {
        super(plugin);
    }

    @Override
    public String getAddonName() {
        return "SignTP";
    }

    @Override
    public void onEnable() {
        int max = plugin.getConfig().getInt("signTP.mysql.maxconection", 2);
        String host = plugin.getConfig().getString("signTP.mysql.host", "localhost");
        String user = plugin.getConfig().getString("signTP.mysql.username", "minecraft");
        String pass = plugin.getConfig().getString("signTP.mysql.password", "123456");
        String db = plugin.getConfig().getString("signTP.mysql.database", "minecraft");
        datasource = new SignTpDataSource(host, user, pass, db, max, this);

        plugin.getServer().getPluginManager().registerEvents(new SignTPListener(this), plugin);
    }

    @Override
    public void onDisable() {
    }

    public SignTpDataSource getDataSource() {
        return datasource;
    }

}
