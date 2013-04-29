package me.kime.kc.Portal;

import me.kime.kc.KimeCraft;

/**
 *
 * @author Kime
 */
public class Portal {

    private final KimeCraft plugin;

    public Portal(KimeCraft instance) {
        this.plugin = instance;
        
        plugin.getPluginManager().registerEvents(new PortalLinstener(this), plugin);
    }

    public KimeCraft getPlugin() {
        return plugin;
    }
}
