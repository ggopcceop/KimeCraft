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
package me.kime.kc;

import java.util.HashMap;
import me.kime.kc.locale.LocaleManager;
import me.kime.kc.addon.mine.Mine;
import me.kime.kc.util.KLogger;
import me.kime.kc.util.KMessager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class KCCommand implements CommandExecutor {

    private final KimeCraft plugin;
    private final long cooldown = 30 * 60 * 1000;
    private final HashMap<String, CD> cooldownData;
    private World mineWorld;

    public KCCommand(KimeCraft instance) {
        this.plugin = instance;
        cooldownData = new HashMap<>();
        Mine mine = (Mine) plugin.getAddon("Mine");
        if (mine != null) {
            mineWorld = mine.getMineWorld();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {

        switch (command.getName()) {
            case "city":
                if (!(sender instanceof Player)) {
                    return true;
                }
                Player player = (Player) sender;
                KPlayer kPlayer = plugin.getOnlinePlayer(player.getName());
                World world = player.getLocation().getWorld();

                CD cd = cooldownData.get(player.getName().toLowerCase());
                if (cd == null) {
                    cd = new CD();
                    cooldownData.put(player.getName().toLowerCase(), cd);
                }

                long time = System.currentTimeMillis() - cd.getCityCD();
                if (time >= cooldown) {
                    if (world.equals(plugin.getDefaultWorld()) || world.equals(mineWorld)) {
                        Location loc = new Location(plugin.getDefaultWorld(), -308, 216, 22, 0, 0);
                        player.getPlayer().teleport(loc, TeleportCause.COMMAND);

                        KMessager.sendMessage(kPlayer, ChatColor.YELLOW, "city_success");
                        cd.setCityCD(System.currentTimeMillis());
                    } else {
                        KMessager.sendError(kPlayer, "city_disallow");
                    }
                } else {
                    KMessager.sendError(kPlayer, "city_cooldown", "" + (((cooldown - time) / 1000)));
                }
                break;
            case "kimecraft":
                if (split.length == 1 && split[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("kc.admin.reload")) {
                        plugin.reloadAddons();
                        if (sender instanceof Player) {
                            kPlayer = plugin.getOnlinePlayer(sender.getName());
                            KMessager.sendMessage(kPlayer, ChatColor.GREEN, "reload", "KimeCraft");
                        } else {
                            KLogger.info(LocaleManager.getDefauLocale().phrase("reload", "KimeCraft"));
                        }

                    }
                } else if (split.length == 2 && split[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("kc.admin.reload")) {
                        Addon addon = plugin.getAddon(split[1].toLowerCase());
                        if (addon != null) {
                            addon.onReload();
                            if (sender instanceof Player) {
                                kPlayer = plugin.getOnlinePlayer(sender.getName());
                                KMessager.sendMessage(kPlayer, ChatColor.GREEN, "reload", addon.getAddonName());
                            } else {
                                KLogger.info(LocaleManager.getDefauLocale().phrase("reload", addon.getAddonName()));
                            }
                        }
                    }
                }
                break;
        }
        return true;
    }

    class CD {

        private long city_cd = 0;

        public long getCityCD() {
            return this.city_cd;
        }

        public void setCityCD(long cd) {
            this.city_cd = cd;
        }
    }
}
