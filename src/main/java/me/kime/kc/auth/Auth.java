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
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;

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
    private final AuthListener authListener;
    private final AuthCommand authCommand;
    private CommandExecutor defaultCommandExecutor;
    private TabCompleter defaultTabCompleter;

    public Auth(KimeCraft plugin) {
        super(plugin);
        authListener = new AuthListener(this);
        authCommand = new AuthCommand(this);
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
        defaultCommandExecutor = plugin.getCommand("login").getExecutor();
        defaultTabCompleter = plugin.getCommand("login").getTabCompleter();
        
        plugin.getCommand("login").setExecutor(authCommand);
        plugin.getCommand("login").setTabCompleter(authCommand);

        //login event
        plugin.getPluginManager().registerEvents(authListener, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(authListener);
        plugin.getCommand("login").setExecutor(defaultCommandExecutor);
        plugin.getCommand("login").setTabCompleter(defaultTabCompleter);
    }

    @Override
    public void onReload() {
        databaseKey = plugin.getConfig().getString("auth.mysql", "auth");
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

}
