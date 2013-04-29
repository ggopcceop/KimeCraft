package me.kime.kc.SignTP;

import me.kime.kc.KimeCraft;

public class SignTP {

    private KimeCraft plugin;
    private final SignTpDataSource datasource;

    public SignTP(KimeCraft plugin) {
        this.plugin = plugin;

        int max = plugin.getConfig().getInt("signTP.mysql.maxconection", 2);
        String host = plugin.getConfig().getString("signTP.mysql.host", "localhost");
        String user = plugin.getConfig().getString("signTP.mysql.username", "minecraft");
        String pass = plugin.getConfig().getString("signTP.mysql.password", "123456");
        String db = plugin.getConfig().getString("signTP.mysql.database", "minecraft");
        datasource = new SignTpDataSource(host, user, pass, db, max, this);

        plugin.getServer().getPluginManager().registerEvents(new SignTPListener(this), plugin);

    }

    public KimeCraft getPlugin() {
        return plugin;
    }

    public SignTpDataSource getDataSource() {
        return datasource;
    }
}
