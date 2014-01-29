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
package me.kime.kc.auth;

import me.kime.kc.Addon;
import me.kime.kc.KPlayer;
import me.kime.kc.KimeCraft;

/**
 * Auth plugin
 *
 * @author Kime
 *
 */
public class Auth extends Addon {

    private final long sessionTime = 1000 * 120;
    private AuthDataSource dataSource = null;
    private String databaseKey;

    public Auth(KimeCraft plugin) {
        super(plugin);
    }

    public KPlayer getOnlinePlayer(String name) {
        return plugin.getOnlinePlayer(name);
    }

    public AuthDataSource getDataSource() {
        return dataSource;
    }

    public long getSessionTime() {
        return sessionTime;
    }

    @Override
    public String getAddonName() {
        return "Auth";
    }

    @Override
    public void onEnable() {
        //start sql connection
        databaseKey = plugin.getConfig().getString("auth.mysql", "auth");

        dataSource = new AuthDataSource(databaseKey);

        //login command executor
        AuthCommand command = new AuthCommand(this);
        plugin.getCommand("login").setExecutor(command);
        plugin.getCommand("login").setTabCompleter(command);

        //login event
        plugin.getPluginManager().registerEvents(new AuthListener(this), plugin);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onReload() {
        databaseKey = plugin.getConfig().getString("auth.mysql", "auth");
    }
}
