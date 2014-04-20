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
public class DynamicCallableTask extends DynamicTask {

    private final ExecutorService pool;

    public DynamicCallableTask(ExecutorService pool, KimeCraft plugin) {
        super(plugin);
        this.pool = pool;
    }

    @Override
    public DynamicCallableTask call(Callable call) {
        return (DynamicCallableTask) super.call(call);
    }

    @Override
    public DynamicCallableTask call(CallableVoid call) {
        return (DynamicCallableTask) super.call(call);
    }

    @Override
    public DynamicCallableTask error(CallableError call) {
        return (DynamicCallableTask) super.error(call);
    }

    @Override
    public DynamicCallableTask done(Callable call) {
        return (DynamicCallableTask) super.done(call);
    }

    @Override
    public DynamicCallableTask done(CallableVoid call) {
        return (DynamicCallableTask) super.done(call);
    }

    public Caller caller() {
        return new CallerImpl(this, pool);
    }
}
