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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import me.kime.kc.KimeCraft;

/**
 *
 * @author Kime
 */
public class async {

    private static ExecutorService pool;
    private static KimeCraft plugin;
    private static Map<String, Task> taskStore;
    private static Byte lock;

    public static void init(ExecutorService e, KimeCraft k) {
        pool = e;
        plugin = k;
        lock = Byte.MAX_VALUE;
        taskStore = new HashMap<>();
    }

    public static Caller register(String key, Task task) {
        if (key != null && task != null) {
            taskStore.put(key, task);
            return new CallerImpl(task, pool);
        }
        return null;
    }

    public static Caller listen(String key) {
        Task task = taskStore.get(key);
        if (task != null) {
            return new CallerImpl(task, pool);
        }
        return null;
    }

    public static DynamicExecutableTask on() {
        return new DynamicExecutableTask(pool, plugin);
    }

    public static DynamicTask on(String key) {
        DynamicTask task = new DynamicExecutableTask(pool, plugin);
        taskStore.put(key, task);
        return task;
    }

    public static DynamicCallableTask call() {
        return new DynamicCallableTask(pool, plugin);
    }

    public static void close() {
        taskStore.clear();
        pool.shutdown();
    }

}
