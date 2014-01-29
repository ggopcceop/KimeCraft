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

package me.kime.kc.database;

import java.util.HashMap;

/**
 *
 * @author Kime
 */
public class DataSourceManager {

    private final static HashMap<String, DataSource> dataSources = new HashMap<>();

    public static DataSource createDataSource(String key, String host, String user, String pass, String db, int maxConnections) {
        DataSource dataSource = dataSources.get(key);
        if (dataSource == null) {
            dataSource = new DataSource(host, user, pass, db, maxConnections);
            dataSources.put(key, dataSource);
        }
        return dataSource;
    }

    public static DataSource getDataSource(String key) {
        return dataSources.get(key);
    }
}
