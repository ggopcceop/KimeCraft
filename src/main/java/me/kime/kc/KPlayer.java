package me.kime.kc;

import me.kime.kc.Auth.PlayerCache;
import me.kime.kc.Util.KCLogger;
import me.kime.kc.Util.KCTPer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * KPlayer additional info store
 *
 * @author Kime
 *
 */
public class KPlayer {

    private Player player = null;
    private boolean isAuth;
    private String LoginIp;
    private String password;
    private long LoginDate;
    private String salt = null;
    private int groupId = -1;
    private PlayerCache cache;
    private int timeoutTaskId;
    private int lastMineId = -1;
    private double payRate = 0;
    private double salary = 0;
    private double totalSalary = 0;
    private int borderCheckTaskId;
    private double lastX;
    private double lastY;
    private double lastZ;
    private float lastYaw;
    private float lastPitch;
    private StringBuilder TypedPassword;
    private int TypedPasswordLenght = 0;

    public KPlayer(Player player) {
        this.player = player;
        isAuth = false;
        borderCheckTaskId = -1;
        timeoutTaskId = -1;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public Player getPlayer() {
        return player;
    }

    public void setLoginIp(String ip) {
        this.LoginIp = ip;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginDate(long date) {
        this.LoginDate = date;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public int getGroup() {
        return groupId;
    }

    public String getLoginIp() {
        return LoginIp;
    }

    public long getLoginDate() {
        return LoginDate;
    }

    public String getNameLowCase() {
        return player.getName().toLowerCase();
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

    public void setLastMine(int id) {
        lastMineId = id;
    }

    public int getLastMine() {
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

    void setBorderCheckTaskId(int borderCheckTaskId) {
        this.borderCheckTaskId = borderCheckTaskId;
    }

    int getBorderCheckTaskId() {
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
}
