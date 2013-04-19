package me.kime.kc.Auth;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.kime.kc.KPlayer;
import me.kime.kc.Auth.Hash.PasswordSecurity;
import me.kime.kc.Util.KCLogger;
import me.kime.kc.Util.KCMessager;

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

    private Auth auth;

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

        if (split.length != 1) {
            KCMessager.sentError(player, "Usage /login [password]");
            return true;
        }

        KPlayer kPlayer = auth.getOnlinePlayer(player.getName());

        kPlayer.setTypedPassword(split[0]);
        String password = kPlayer.getTypedPassword();


        if (kPlayer.isAuth()) {
            KCMessager.sentMessage(player, "You are already login!", ChatColor.GREEN);
            return true;
        }

        String hash = kPlayer.getPassword();
        String salt = kPlayer.getSalt();
        int group = kPlayer.getGroup();
        if (hash == null || group == -1) {
            KCMessager.sentError(kPlayer, "Please regiter at www.e7play.co!");
            return true;
        }
        try {
            if (group > 3 && group < 10) {
                KCMessager.sentError(kPlayer, "You are NOT an activate user!");
                return true;
            }
            if (PasswordSecurity.comparePasswordWithHash(password, hash, salt)) {

                kPlayer.restoreCache();
                kPlayer.setAuth(true);

                KCMessager.sentMessage(player, "Successful login!", ChatColor.GREEN);
                KCLogger.info(player.getDisplayName() + " logged in!");
            } else {
                KCLogger.info(player.getDisplayName() + " used the wrong password");
                KCMessager.sentError(player, "Wrong password!");
            }
        } catch (NoSuchAlgorithmException ex) {
            KCLogger.showError(ex.getMessage());
            KCMessager.sentError(player, "An error ocurred; Please contact the admin");
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
