package me.Kime.KC.Util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * plugin logger
 *
 * @author Kime
 *
 */
public class KCLogger {

    private static final Logger log = Logger.getLogger("Minecraft");

    public static void info(String msg) {
        log.log(Level.INFO, "[KimeCraft] {0}", msg);
    }

    public static void showError(String msg) {
        log.log(Level.SEVERE, "[KimeCraft] [Error] {0}", msg);
    }
}
