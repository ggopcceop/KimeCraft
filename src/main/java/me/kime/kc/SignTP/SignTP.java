package me.kime.kc.SignTP;

import me.kime.kc.KC;

public class SignTP {

    private KC plugin;
    private final SignTpDataSource datasource;

    public SignTP(KC plugin) {
        this.plugin = plugin;

        datasource = new SignTpDataSource(this, plugin.getConfig());

        plugin.getServer().getPluginManager().registerEvents(new SignTPListener(this), plugin);

    }

    public KC getPlugin() {
        return plugin;
    }

    public SignTpDataSource getDataSource() {
        return datasource;
    }
}
