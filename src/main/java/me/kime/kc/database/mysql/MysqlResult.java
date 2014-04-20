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
package me.kime.kc.database.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import me.kime.kc.database.DataSource;
import me.kime.kc.database.SimpleResult;
import me.kime.kc.database.functionInterface.Errors;
import me.kime.kc.database.functionInterface.Query;
import me.kime.kc.database.functionInterface.Responce;
import me.kime.kc.database.functionInterface.ResponceVoid;

/**
 *
 * @author Kime
 */
public class MysqlResult extends SimpleResult<Connection, ResultSet, String> {

    public MysqlResult(Query request, DataSource source) {
        super(request, source);
    }

    @Override
    public MysqlResult onDone(Responce<ResultSet> responce) {
        this.responce = responce;
        return this;
    }

    @Override
    public MysqlResult onDone(ResponceVoid responce) {
        this.responce = responce;
        return this;
    }

    @Override
    public MysqlResult onError(Errors<String> error) {
        this.error = error;
        return this;
    }
}
