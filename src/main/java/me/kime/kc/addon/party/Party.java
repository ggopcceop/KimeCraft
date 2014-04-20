/*
 * Copyright (C) 2014 Kime
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.kime.kc.addon.party;

import java.util.HashMap;
import me.kime.kc.Addon;
import me.kime.kc.KimeCraft;

/**
 *
 * @author Kime
 */
public class Party extends Addon {
    
    private final HashMap<String, PartyData> parties;

    public Party(KimeCraft instance) {
        super(instance);
        parties = new HashMap<>();
    }

    @Override
    public String getAddonName() {
        return "Party";
    }

    @Override
    public void onEnable() {
        PartyCommand noobCommand = new PartyCommand(this);
        plugin.getCommand("party").setExecutor(noobCommand);
        plugin.getPluginManager().registerEvents(new PartyListener(this), plugin);
    }

    @Override
    public void onDisable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
