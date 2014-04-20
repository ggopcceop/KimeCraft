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

import java.util.List;
import me.kime.kc.addon.auth.PlayerCache;
import me.kime.kc.locale.Locale;
import me.kime.kc.util.KCTPer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * KPlayer additional info store
 *
 * @author Kime
 *
 */
public class KPlayer {

    public final Player player;

    public KPlayer(Player player) {
        this.player = player;
        isAuth = false;
        borderCheckTaskId = -1;
        timeoutTaskId = -1;
    }

    public Player getPlayer() {
        return player;
    }

    public String getNameLowCase() {
        return player.getName().toLowerCase();
    }

    //=============== the mining pay ================//
    private Material lastMineId = null;
    private double payRate = 0;
    private double salary = 0;
    private double totalSalary = 0;

    public void setLastMine(Material material) {
        lastMineId = material;
    }

    public Material getLastMine() {
        return lastMineId;
    }

    public void addSalary(double mount) {
        this.salary += mount;
    }

    public double getSalary(boolean clear) {
        double mount = salary;
        if (clear) {
            totalSalary += salary;
            salary = 0;
        }
        return mount;
    }

    public double getPayRate() {
        return payRate;
    }

    public void setPayRate(double payRate) {
        this.payRate = payRate;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    //=============== the border checking ================//
    private int borderCheckTaskId;
    private double lastX;
    private double lastY;
    private double lastZ;
    private float lastYaw;
    private float lastPitch;

    public void setBorderCheckTaskId(int borderCheckTaskId) {
        this.borderCheckTaskId = borderCheckTaskId;
    }

    public int getBorderCheckTaskId() {
        return borderCheckTaskId;
    }

    public void setLastLoc() {
        Location loc = player.getLocation();
        this.lastX = loc.getX();
        this.lastY = loc.getY();
        this.lastZ = loc.getZ();
        this.lastYaw = loc.getYaw();
        this.lastPitch = loc.getPitch();
    }

    public void tpLastLoc() {
        Location loc = new Location(player.getWorld(), lastX, lastY, lastZ, lastYaw, lastPitch);
        KCTPer.tp(player, loc);
    }

    //=============== the locale of the player================//
    private Locale locale;

    /**
     * set the locale of the player
     *
     * @param locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * get the locale of the player
     *
     * @return null if there is not locale
     */
    public Locale getLocale() {
        return locale;
    }

    //============== auth functions of player ==============//
    public boolean isAuth;
    public String LoginIp;
    public String password;
    public long LoginDate;
    public String salt = null;
    public int groupId = -1;
    private PlayerCache cache;
    private int timeoutTaskId;

    private StringBuilder TypedPassword;
    private int TypedPasswordLenght = 0;

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public void cache() {
        cache = new PlayerCache();
        cache.cacheInventory(player.getInventory());
        cache.cacheStatus(player);
    }

    public void restoreCache() {
        if (cache != null) {
            cache.restoreInventory(player.getInventory());
            cache.restoreStatus(player);
            cache = null;
        }
    }

    public void setTimeoutTaskId(int timeoutTaskId) {
        this.timeoutTaskId = timeoutTaskId;
    }

    public int getTimeoutTaskId() {
        return timeoutTaskId;
    }

    /**
     * store the login password player typed
     *
     * @param pass
     * @return
     */
    public String setTypedPassword(String pass) {
        if (TypedPasswordLenght == 0) {
            this.TypedPasswordLenght = pass.length();
            this.TypedPassword = new StringBuilder();
            TypedPassword.append(pass);
        } else {
            char[] array = pass.toCharArray();
            int i = 0;
            while (i < TypedPasswordLenght && i < pass.length()) {
                if (array[i] != '*') {
                    break;
                }
                i++;
            }
            if (i < TypedPasswordLenght) {
                TypedPassword.delete(i, TypedPassword.length());
            }
            while (i < pass.length()) {
                TypedPassword.append(array[i]);
                i++;
            }

            TypedPasswordLenght = TypedPassword.length();
        }
        StringBuilder replace = new StringBuilder();
        for (int i = 0; i < pass.length(); i++) {
            replace.append("*");
        }
        return replace.toString();
    }

    public String getTypedPassword() {
        if (TypedPassword == null) {
            return null;
        } else {
            String p = TypedPassword.toString();
            TypedPassword = null;
            TypedPasswordLenght = 0;
            return p;
        }
    }

    //=========== chating functions ================//
    public int currentChannel = 0;
    private List<Integer> reigsteredChannels;

}
