package me.kime.kc.Auth;

import java.sql.SQLException;
import java.util.HashMap;

import me.kime.kc.KC;
import me.kime.kc.KPlayer;
import me.kime.kc.Util.KCLogger;

/**
 * Auth plugin
 *
 * @author Kime
 *
 */
public class Auth {

    private final long sessionTime = 1000 * 120;
    private KC plugin;
    private DataSource dataSource = null;

    public Auth(KC instance, HashMap<String, KPlayer> onlineList) {
        this.plugin = instance;

        //start sql connection
        try {
            dataSource = new DataSource(plugin.getConfig());
        } catch (ClassNotFoundException | SQLException e) {
            KCLogger.showError(e.getMessage());
        }

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

    public DataSource getDataSource() {
        return dataSource;
    }

    public KC getPlugin() {
        return plugin;
    }

    public long getSessionTime() {
        return sessionTime;
    }
}
