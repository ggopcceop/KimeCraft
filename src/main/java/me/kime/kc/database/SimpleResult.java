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

import me.kime.kc.database.functionInterface.Query;
import me.kime.kc.database.functionInterface.Responce;
import me.kime.kc.database.functionInterface.Errors;
import me.kime.kc.task.async.async;

/**
 *
 * @author Kime
 */
public abstract class SimpleResult<T, R, U> implements Result<R, U> {

    protected final DataSource dataSource;
    protected Query request;
    protected Responce responce;
    protected Errors error;

    protected SimpleResult(Query<T, R> request, DataSource db) {
        this.request = request;
        this.dataSource = db;
    }

    @Override
    public void execute() {
        async.on().call(t -> {
            t.put("result", dataSource.execute(request));
        }).done(t -> {
            R result = (R) t.get("result");
            if (result != null) {
                responce.accept(result);
            }
        }).error(t -> {
            error.onError(t.getException());
        }).execute();
    }

}
