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
import me.kime.kc.database.Result;
import me.kime.kc.database.functionInterface.Errors;
import me.kime.kc.database.functionInterface.Query;
import me.kime.kc.database.functionInterface.Responce;
import me.kime.kc.database.functionInterface.ResponceVoid;
import me.kime.kc.task.async.async;

/**
 *
 * @author Kime
 * @param <R>
 */
public class MongoResult<R> implements Result<R, String> {

    private final MongoDataSource dataSource;

    private final Query request;
    private Responce responce;
    private Errors error;

    public MongoResult(Query<DB, R> request, MongoDataSource source) {
        this.dataSource = source;
        this.request = request;
    }

    @Override
    public MongoResult onDone(Responce<R> responce) {
        this.responce = responce;
        return this;
    }

    @Override
    public MongoResult onDone(ResponceVoid responce) {
        this.responce = responce;
        return this;
    }

    @Override
    public MongoResult onError(Errors<String> error) {
        this.error = error;
        return this;
    }

    @Override
    public void execute() {
        async.on().call(t -> {
            t.put("result", dataSource.execute(request));
        }).done(t -> {
            Object result = t.get("result");
            responce.accept(result);
        }).error(t -> {
            error.onError(t.getException());
        }).execute();
    }
}
