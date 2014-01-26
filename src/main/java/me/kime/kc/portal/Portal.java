package me.kime.kc.portal;

import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;

/**
 *
 * @author Kime
 */
public class Portal extends Addon {

    public Portal(KimeCraft instance) {
        super(instance);
    }

    @Override
    public String getAddonName() {
        return "Portal";
    }

    @Override
    public void onEnable() {
        plugin.getPluginManager().registerEvents(new PortalLinstener(this), plugin);
    }

    @Override
    public void onDisable() {

    }
}
