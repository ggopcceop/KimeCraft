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
package me.kime.kc.addon.fun;

public class RedstoneC {

    private int count;
    private long date;

    public RedstoneC() {
        count = 0;
        date = 0L;
    }

    public synchronized void add() {
        count++;
    }

    public synchronized boolean isOver(long d) {
        if (count > 15) {
            if (((d - date) / 1000) > 8) {
                date = d;
                count = 0;
                return false;
            }
            //System.out.print(((d - date)/1000) + " " + count);
            return true;
        }
        return false;
    }

    public long getTime() {
        return date;
    }

    public synchronized void setDate(long d) {
        date = d;
    }
}
