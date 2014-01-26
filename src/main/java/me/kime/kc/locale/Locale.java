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

import java.util.HashMap;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Kime
 */
public class Locale {

    private final String language;
    private final HashMap<String, String> translation;

    public Locale(ConfigurationSection config, String locale) {
        this.language = locale;
        translation = new HashMap<>();
        Set<String> keys = config.getKeys(false);
        for (String key : keys) {
            translation.put(key, config.getString(key));
        }
    }

    public String phrase(String key, String... words) {
        String line = translation.get(key);
        if (line == null) {
            if(this == LocaleManager.getDefauLocale()){
                throw new LocaleException("translation is not exists");
            }
            return LocaleManager.getDefauLocale().phrase(key, words);
        } else if (words.length > 0) {
            for (int i = 0; i < words.length; i++) {
                line = line.replaceAll("%" + (i + 1), words[i]);
            }
        }
        return line;
    }

    public String getLanguage() {
        return language;
    }

    public int size() {
        return translation.size();
    }
}
