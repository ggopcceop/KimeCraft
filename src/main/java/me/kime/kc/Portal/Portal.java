package me.kime.kc.Portal;

import me.kime.kc.KC;

/**
 *
 * @author Kime
 */
public class Portal {

    private final KC plugin;

    public Portal(KC instance) {
        this.plugin = instance;
        
        plugin.getPluginManager().registerEvents(new PortalLinstener(this), plugin);
    }

    public KC getPlugin() {
        return plugin;
    }
}
