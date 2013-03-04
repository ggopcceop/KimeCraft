package me.Kime.KC.Util;

import java.util.logging.Logger;

/**
 * plugin logger
 * 
 * @author Kime
 *
 */
public class KCLogger {
	
	private static final Logger log = Logger.getLogger("Minecraft");
	
	public static void info(String msg){
		log.info("[KimeCraft] " + msg);
	}
	
	public static void showError(String msg){
		log.severe("[KimeCraft] [Error] " + msg);
	}
}
