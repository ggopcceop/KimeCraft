package me.Kime.KC;

import me.Kime.KC.Auth.PlayerCache;
import org.bukkit.entity.Player;

/**
 * KPlayer
 * additional info store
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
	
	
	public KPlayer(Player player){
		this.player = player;
		isAuth = false;
		timeoutTaskId = -1;
	}
	
	public boolean isAuth(){
		return isAuth;
	}
	
	public void setAuth(boolean auth){
		isAuth = auth;
	}
	
	public Player getPlayer(){
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
	
	public String getNameLowCase(){
		return player.getName().toLowerCase();
	}

	public void cache() {
		cache = new PlayerCache();
		cache.cacheInventory(player.getInventory());
		cache.cacheStatus(player);
	}

	public void restoreCache() {
		if(cache != null){
			cache.restoreInventory(player.getInventory());
			cache.restoreStatus(player);
			cache = null;
		}		
	}

	public void setTimeoutTaskId(int timeoutTaskId) {
		this.timeoutTaskId = timeoutTaskId;		
	}
	
	public int getTimeoutTaskId(){
		return timeoutTaskId;
	}

	public void setLastMine(int id){
		lastMineId = id;
	}
	public int getLastMine() {
		return lastMineId;
	}

	public void addSalary(double mount) {
		this.salary += mount;
	}
	public double getSalary(boolean clear){
		double mount = salary;
		if(clear){
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
	
}
