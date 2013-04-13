package me.Kime.KC.Auth;

import java.sql.SQLException;

import me.Kime.KC.KC;
import me.Kime.KC.KPlayer;
import me.Kime.KC.Util.KCLogger;

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

    public Auth(KC instance) {
        this.plugin = instance;

        config();

        //start sql connection
        try {
            dataSource = new DataSource(plugin.getConfig());
        } catch (ClassNotFoundException | SQLException e) {
            KCLogger.showError(e.getMessage());
        }

        //login command executor
        plugin.getCommand(
                "login").setExecutor(new AuthCommand(this));

        //login event
        plugin.getPluginManager()
                .registerEvents(new AuthLinstener(this), plugin);
    }

    private void config() {

        if (!plugin.getConfig().contains("auth.mysql")) {
            plugin.getConfig().options().copyDefaults(true);
        }

        plugin.getConfig().addDefault("auth.mysql.database", "minecraft");
        plugin.getConfig().addDefault("auth.mysql.host", "localhost");
        plugin.getConfig().addDefault("auth.mysql.port", "3306");
        plugin.getConfig().addDefault("auth.mysql.username", "minecraft");
        plugin.getConfig().addDefault("auth.mysql.password", "minecraft");
        plugin.getConfig().addDefault("auth.mysql.tableName", "pre_ucenter_members");
        plugin.getConfig().addDefault("auth.mysql.columnName", "username");
        plugin.getConfig().addDefault("auth.mysql.columnPassword", "password");
        plugin.getConfig().addDefault("auth.mysql.columnIp", "lloginip");
        plugin.getConfig().addDefault("auth.mysql.columnLastLogin", "llogindate");

        plugin.saveConfig();
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
