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

/**
 *
 * @author Kime
 */
public class LocaleManager {

    private static final HashMap<String, Locale> locales = new HashMap<>();
    private static final String defaultLocale = "en_US";

    public static Locale getLocale(String localeString) {
        Locale locale = locales.get(localeString);
        if (locale == null) {
            locale = LocaleLoader.loadLocale(localeString);
            if (locale != null) {
                locales.put(localeString, locale);
            } else {
                locale = getDefauLocale();
            }
        }
        return locale;
    }

    public static Locale getDefauLocale() {
        return getLocale(defaultLocale);
    }

}
