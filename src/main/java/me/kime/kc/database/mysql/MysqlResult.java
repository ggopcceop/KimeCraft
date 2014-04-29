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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.kime.kc.database.QueryType;
import me.kime.kc.database.Result;
import me.kime.kc.database.functionInterface.Errors;
import me.kime.kc.database.functionInterface.Responce;
import me.kime.kc.database.functionInterface.ResponceVoid;
import me.kime.kc.database.functionInterface.Update;
import me.kime.kc.task.async.async;
import me.kime.kc.util.KLogger;

/**
 *
 * @author Kime
 */
public class MysqlResult implements Result<ResultSet, String> {
    
    protected final MysqlDataSource dataSource;
    
    private final QueryType type;
    private final String sql;
    
    private final Update request;
    private Responce responce;
    private Errors error;
    
    public MysqlResult(Update request, MysqlDataSource source, QueryType type, String sql) {
        dataSource = source;
        
        this.request = request;
        
        this.type = type;
        this.sql = sql;
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
    
    @Override
    public void execute() {
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            
            async.on().call(t -> {
                request.apply(pst);
                if (type == QueryType.QUERY) {
                    t.put("result", pst.executeQuery());
                } else {
                    pst.executeUpdate();
                }
            }).done(t -> {
                ResultSet result = (ResultSet) t.get("result");
                
                if (responce != null) {
                    responce.accept(result);
                }
                
                dataSource.close(result);
                dataSource.close(pst);
                dataSource.close(conn);
                
            }).error(t -> {
                if (error != null) {
                    error.onError(t.getException().getMessage());
                }                
                dataSource.close(pst);
                dataSource.close(conn);
            }).execute();
            
        } catch (SQLException ex) {
            KLogger.showError(ex.getMessage());
        }
    }
}
