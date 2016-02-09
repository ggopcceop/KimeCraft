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
package me.kime.kc.addon.chat;

import me.kime.kc.Addon;
import me.kime.kc.KPlayer;
import me.kime.kc.KimeCraft;
import me.kime.kc.database.DataSourceManager;
import me.kime.kc.database.mysql.MysqlDataSource;
import me.kime.kc.locale.Locale;
import me.kime.kc.locale.LocaleManager;
import me.kime.kc.util.KLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Kime
 */
public class Chat extends Addon {

    private int normalChatRange;
    private final ChatListener chatListener;
    private final ChatCommand chatCommand;
    private CommandExecutor defaultCommandExecutor;
    private String databaseKey;
    private MysqlDataSource dataSource;

    public Chat(KimeCraft plugin) {
        super(plugin);
        chatListener = new ChatListener(this);
        chatCommand = new ChatCommand(this);
    }

    @Override
    public String getAddonName() {
        return "Chat";
    }

    @Override
    public void onEnable() {
        databaseKey = plugin.getConfig().getString("chat.mysql", "minecraft");
        dataSource = DataSourceManager.getMysqlDataSource(databaseKey);
        if (dataSource == null) {
            KLogger.showError("Chat fail to get data source " + databaseKey);
        }

        normalChatRange = plugin.config.getInt("chat.normalChatRange", 50);

        defaultCommandExecutor = plugin.getCommand("chat").getExecutor();
        plugin.getCommand("chat").setExecutor(chatCommand);

        plugin.getPluginManager().registerEvents(chatListener, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(chatListener);
        plugin.getCommand("chat").setExecutor(defaultCommandExecutor);
    }

    public void registerDynmapChatEvent() {
        plugin.getPluginManager().registerEvents(new DynmapChatListener(this), plugin);
    }

    public void rangeChat(String sender, Location loc, int range, String message) {
        World currentWorld = loc.getWorld();
        int squaredRange = range * range;
        String msgString = "[" + sender + "]: " + message;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == currentWorld) {
                if (loc.distanceSquared(player.getLocation()) <= squaredRange) {
                    player.sendMessage(msgString);
                }
            }
        }
        KLogger.info("[nor][" + sender + "]: " + message);
    }

    public void publicChat(Player sender, String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            StringBuilder result = new StringBuilder();
            KPlayer kPlayer = plugin.getOnlinePlayer(player.getName());
            Locale locale = kPlayer.getLocale();

            if (locale == null) {
                locale = LocaleManager.getDefauLocale();
            }

            result.append(ChatColor.GOLD.toString());
            result.append("[");
            result.append(locale.phrase("chat_public"));
            result.append("][");
            result.append(sender.getName());
            result.append("]: ");
            result.append(message);

            player.sendMessage(result.toString());
        }
        KLogger.info("[pub][" + sender.getName() + "]: " + message);

        if (plugin.dynampIntegration) {
            plugin.getDynmap().sendBroadcastToWeb(sender.getName(), message);
        }
    }

    public void webChat(String sender, String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            StringBuilder result = new StringBuilder();
            KPlayer kPlayer = plugin.getOnlinePlayer(player.getName());
            Locale locale = kPlayer.getLocale();

            if (locale == null) {
                locale = LocaleManager.getDefauLocale();
            }

            result.append(ChatColor.GOLD.toString());
            result.append("[");
            result.append(locale.phrase("chat_web"));
            result.append("][");
            result.append(sender);
            result.append("]: ");
            result.append(message);

            player.sendMessage(result.toString());
        }
        KLogger.info("[web][" + sender + "]: " + message);
    }

    public void channelChat(Entity sender, String channel, String message) {

    }

    public Channel registerChannel(String name) {
        Channel channel = new Channel(name);
        return channel;
    }

    public int getNormalChatRange() {
        return normalChatRange;
    }

    public MysqlDataSource getDataSource() {
        return dataSource;
    }
}
