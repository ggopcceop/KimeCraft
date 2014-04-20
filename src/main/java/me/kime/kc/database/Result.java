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

import me.kime.kc.database.functionInterface.ResponceVoid;
import me.kime.kc.database.functionInterface.Responce;
import me.kime.kc.database.functionInterface.Errors;

/**
 *
 * @author Kime
 * @param <T>
 * @param <U>
 */
public interface Result<T, U> {

    public Result onDone(Responce<T> responce);

    public Result onDone(ResponceVoid responce);

    public Result onError(Errors<U> error);

    public void execute();
}
