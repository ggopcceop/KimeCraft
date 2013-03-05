package me.Kime.KC.Util;

import me.Kime.KC.KPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * plugin messager to send player message
 *
 * @author Kime
 *
 */
public class KCMessager {

    public static void sentError(Player player, String msg) {
        if (player != null) {
            player.sendMessage(ChatColor.RED + msg);
        }
    }

    public static void sentError(KPlayer kPlayer, String msg) {
        if (kPlayer != null) {
            sentError(kPlayer.getPlayer(), msg);
        }
    }

    public static void sentMessage(Player player, String msg, ChatColor color) {
        if (player != null) {
            player.sendMessage(color + msg);
        }
    }
}
