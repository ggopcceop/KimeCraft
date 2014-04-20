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
package me.kime.kc.task.async;

import java.util.concurrent.ExecutorService;
import me.kime.kc.KimeCraft;

/**
 *
 * @author Kime
 */
public class DynamicExecutableTask extends DynamicTask {

    protected final ExecutorService pool;

    public DynamicExecutableTask(ExecutorService pool, KimeCraft plugin) {
        super(plugin);
        this.pool = pool;
    }

    @Override
    public DynamicExecutableTask call(Callable call) {
        return (DynamicExecutableTask) super.call(call);
    }

    @Override
    public DynamicExecutableTask call(CallableVoid call) {
        return (DynamicExecutableTask) super.call(call);
    }

    @Override
    public DynamicExecutableTask error(CallableError call) {
        return (DynamicExecutableTask) super.error(call);
    }

    @Override
    public DynamicExecutableTask done(Callable call) {
        return (DynamicExecutableTask) super.done(call);
    }

    @Override
    public DynamicExecutableTask done(CallableVoid call) {
        return (DynamicExecutableTask) super.done(call);
    }

    public void execute() {
        new CallerImpl(this, pool).call();
    }
}
