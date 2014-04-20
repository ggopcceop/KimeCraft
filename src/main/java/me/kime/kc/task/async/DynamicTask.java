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

import me.kime.kc.KimeCraft;
import me.kime.kc.util.KLogger;
import org.bukkit.Bukkit;

/**
 *
 * @author Kime
 */
public abstract class DynamicTask implements Task {

    protected Callable call;
    protected CallableError error;
    protected Callable done;

    protected final KimeCraft plugin;

    public DynamicTask(KimeCraft plugin) {
        this.plugin = plugin;
    }

    public DynamicTask call(CallableVoid call) {
        call((t -> {
            call.onCall();
        }));
        return this;
    }

    @Override
    public DynamicTask call(Callable call) {
        this.call = call;
        return this;
    }

    @Override
    public DynamicTask error(CallableError call) {
        this.error = call;
        return this;
    }

    public DynamicTask done(CallableVoid call) {
        done((t -> {
            call.onCall();
        }));
        return this;
    }

    @Override
    public DynamicTask done(Callable call) {
        this.done = call;
        return this;
    }

    @Override
    public void run() {
        try {
            if (call != null) {
                Consumer consumer = new Consumer();

                call.onCall(consumer);

                if (done != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        try {
                            done.onCall(consumer);
                        } catch (Exception ex) {
                        }
                    });
                }
            } else {
                throw new NullPointerException("Nothing to call");
            }
        } catch (Exception ex) {
            if (error != null) {
                error.onError(new Error(ex));
            } else {
                KLogger.showError(ex.getMessage());
            }
        }
    }

}
