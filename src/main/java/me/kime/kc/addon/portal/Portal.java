package me.kime.kc.addon.portal;

import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Kime
 */
public class Portal extends Addon {

    private final PortalLinstener portalListener;
    
    public Portal(KimeCraft instance) {
        super(instance);
        portalListener = new PortalLinstener(this);
    }
    
    @Override
    public String getAddonName() {
        return "Portal";
    }
    
    @Override
    public void onEnable() {
        plugin.getPluginManager().registerEvents(portalListener, plugin);
    }
    
    @Override
    public void onDisable() {
        HandlerList.unregisterAll(portalListener);
    }
}
