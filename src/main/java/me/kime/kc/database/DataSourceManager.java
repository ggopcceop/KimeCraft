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
import me.kime.kc.KimeCraft;
import me.kime.kc.database.mongodb.MongoDataSource;
import me.kime.kc.database.mysql.MysqlDataSource;

/**
 *
 * @author Kime
 */
public class DataSourceManager {

    private final static HashMap<String, MysqlDataSource> mysqldataSources = new HashMap<>();
    private final static HashMap<String, MongoDataSource> mongodbdataSources = new HashMap<>();
    private static KimeCraft plugin;

    public static void init(KimeCraft p) {
        plugin = p;
    }

    public static MysqlDataSource createMysqlDataSource(String key, String host, String user, String pass, String db, int maxConnections) {
        MysqlDataSource dataSource = mysqldataSources.get(key);
        if (dataSource == null) {
            dataSource = new MysqlDataSource(plugin, host, user, pass, db, maxConnections);
            mysqldataSources.put(key, dataSource);
        }
        return dataSource;
    }

    public static MongoDataSource createMongodbDataSource(String key, String host, String user, String pass, String db, int maxConnections) {
        MongoDataSource dataSource = mongodbdataSources.get(key);
        if (dataSource == null) {
            dataSource = new MongoDataSource(plugin, host, user, pass, db, maxConnections);
            mongodbdataSources.put(key, dataSource);
        }
        return dataSource;
    }

    public static MysqlDataSource getMysqlDataSource(String key) {
        return mysqldataSources.get(key);
    }

    public static MongoDataSource getMongolDataSource(String key) {
        return mongodbdataSources.get(key);
    }

}
