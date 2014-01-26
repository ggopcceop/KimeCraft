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
package me.kime.kc.auth;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import me.kime.kc.KPlayer;
import me.kime.kc.util.KLogger;
import me.kime.kc.util.KMessager;
import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 * login command executer
 *
 * @author Kime
 *
 */
public class AuthCommand implements CommandExecutor, TabCompleter {

    private final Auth auth;

    public AuthCommand(Auth instance) {
        this.auth = instance;
    }

    //login command executer
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        KPlayer kPlayer = auth.getOnlinePlayer(player.getName());

        if (split.length != 1) {
            KMessager.sendError(kPlayer, "auth_commandUsage");
            return true;
        }

        kPlayer.setTypedPassword(split[0]);
        String password = kPlayer.getTypedPassword();

        if (kPlayer.isAuth) {
            KMessager.sendMessage(kPlayer, ChatColor.GREEN, "auth_alreadyLogin");
            return true;
        }

        String hash = kPlayer.password;
        String salt = kPlayer.salt;
        int group = kPlayer.groupId;
        if (hash == null || group == -1) {
            KMessager.sendError(kPlayer, "auth_askRegister");
            return true;
        }
        try {
            if (group > 3 && group < 10) {
                KMessager.sendError(kPlayer, "auth_bannedUser");
                return true;
            }
            if (PasswordSecurity.comparePasswordWithHash(password, hash, salt)) {

                kPlayer.restoreCache();
                kPlayer.isAuth = true;

                KMessager.sendMessage(kPlayer, ChatColor.GREEN, "auth_successfulLogin");
                KLogger.info(player.getDisplayName() + " logged in!");

                //call motd event
                Bukkit.getPluginManager().callEvent(new AuthSucceedEvent(kPlayer));
            } else {
                KMessager.sendError(kPlayer, "auth_wrongPassword");
                KLogger.info(player.getDisplayName() + " used the wrong password");
            }
        } catch (NoSuchAlgorithmException ex) {
            KLogger.showError(ex.getMessage());
            KMessager.sendError(kPlayer, "error");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] split) {

        if (split.length == 1) {
            if (sender instanceof Player) {
                KPlayer player = auth.getOnlinePlayer(sender.getName());
                String star = player.setTypedPassword(split[0]);
                return Arrays.asList(new String[]{star});
            }
        }

        return null;
    }
}
