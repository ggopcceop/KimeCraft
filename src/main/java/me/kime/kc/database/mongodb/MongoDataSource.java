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
package me.kime.kc.database.mongodb;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import me.kime.kc.KimeCraft;
import me.kime.kc.database.functionInterface.Query;
import me.kime.kc.database.Result;
import me.kime.kc.database.functionInterface.Update;
import me.kime.kc.util.KLogger;

/**
 *
 * @author Kime
 */
public class MongoDataSource {

    private DB db;

    public MongoDataSource(KimeCraft plugin, String host, String user, String pass, String databaseName, int maxConnections) {
        try {
            MongoCredential credential = MongoCredential.createMongoCRCredential(user, databaseName, pass.toCharArray());
            MongoClient mongoClient = new MongoClient(new ServerAddress(host), Arrays.asList(credential));
            db = mongoClient.getDB(databaseName);
        } catch (UnknownHostException ex) {
            KLogger.showError("Unable to connect Mongodb host:" + host);
        }
    }

    public MongoResult query(Query<DB, DBObject> request) {
        MongoResult mongoResult = new MongoResult(request, this);
        return mongoResult;
    }

    public Result update(Update<DB> request) {
        return query(t -> {
            request.apply(t);
            return null;
        });
    }

    public DBObject execute(Query<DB, DBObject> request) throws Exception {
        return request.apply(db);
    }

}
