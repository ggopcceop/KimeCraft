package me.kime.kc.Auth;

import java.util.HashMap;

import me.kime.kc.KC;
import me.kime.kc.KPlayer;

/**
 * Auth plugin
 *
 * @author Kime
 *
 */
public class Auth {

    private final long sessionTime = 1000 * 120;
    private KC plugin;
    private AuthDataSource dataSource = null;

    public Auth(KC instance, HashMap<String, KPlayer> onlineList) {
        this.plugin = instance;

        //start sql connection
        String db = plugin.getConfig().getString("auth.mysql.database");
        String host = plugin.getConfig().getString("auth.mysql.host");
        String user = plugin.getConfig().getString("auth.mysql.username");
        String pass = plugin.getConfig().getString("auth.mysql.password");
        int max = plugin.getConfig().getInt("auth.mysql.maxconection", 2);

        dataSource = new AuthDataSource(host, user, pass, db, max);

        //login command executor
        AuthCommand command = new AuthCommand(this);
        plugin.getCommand("login").setExecutor(command);
        plugin.getCommand("login").setTabCompleter(command);

        //login event
        plugin.getPluginManager()
                .registerEvents(new AuthLinstener(this, onlineList), plugin);
    }

    public KPlayer getOnlinePlayer(String name) {
        return plugin.getOnlinePlayer(name);
    }

    public AuthDataSource getDataSource() {
        return dataSource;
    }

    public KC getPlugin() {
        return plugin;
    }

    public long getSessionTime() {
        return sessionTime;
    }
}
