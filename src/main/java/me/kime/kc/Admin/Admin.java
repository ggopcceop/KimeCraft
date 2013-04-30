package me.kime.kc.Admin;

import me.kime.kc.KimeCraft;

/**
 *
 * @author Kime
 */
public class Admin {

    private final KimeCraft plugin;

    public Admin(KimeCraft instance) {
        this.plugin = instance;
        plugin.getPluginManager().registerEvents(new AdminLinstener(this), plugin);
    }

    public KimeCraft getPlugin() {
        return plugin;
    }
}
