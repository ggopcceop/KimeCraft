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
package me.kime.kc.locale;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Kime
 */
public class LocaleLoader {

    public static Locale loadLocale(String locale) {
        if (locale != null) {
            InputStream fileStream = LocaleLoader.class.getResourceAsStream("/translation/" + locale + ".yml");
            if (fileStream != null) {
                InputStreamReader fileReader = null;
                try {
                    fileReader = new InputStreamReader(fileStream, "UTF8");
                    FileConfiguration translation = YamlConfiguration.loadConfiguration(fileReader);
                    return new Locale(translation.getConfigurationSection("Language"), locale);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(LocaleLoader.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (fileReader != null) {
                            fileReader.close();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(LocaleLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }
}
