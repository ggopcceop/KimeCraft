package me.Kime.KC.SignTP;

import java.io.File;

import me.Kime.KC.KC;

public class SignTP {

    private KC plugin;
    protected static final String MAINDIRECTORY = "plugins/KC";
    protected static final File VERSIONFILE = new File("plugins/KC" + File.separator + "VERSION");

    public SignTP(KC plugin) {
        this.plugin = plugin;

        plugin.getConfig();

        plugin.getServer().getPluginManager().registerEvents(new SignTPListener(this), plugin);

    }

    public KC getPlugin() {
        return plugin;
    }
}
