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
package me.kime.kc.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class KLogFilter implements Filter {

    @Override
    public Filter.Result filter(LogEvent event) {
        if (event.getMessage().toString().toLowerCase().contains("issued server command: /l")) {
            return Filter.Result.DENY;
        }
        return null;
    }

    @Override
    public Filter.Result getOnMismatch() {
        return null;
    }

    @Override
    public Filter.Result getOnMatch() {
        return null;
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object... os) {
        return null;
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Object o, Throwable thrwbl) {
        return null;
    }

    @Override
    public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable thrwbl) {
        return null;
    }

}
