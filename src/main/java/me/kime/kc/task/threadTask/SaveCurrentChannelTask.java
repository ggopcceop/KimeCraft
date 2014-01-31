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
package me.kime.kc.task.threadTask;

import java.util.LinkedList;
import me.kime.kc.KPlayer;
import me.kime.kc.chat.Chat;

/**
 *
 * @author Kime
 */
public class SaveCurrentChannelTask extends Task {

    private final LinkedList<SubTask> list;
    private final Chat chat;

    public SaveCurrentChannelTask(Chat chat) {
        list = new LinkedList<>();
        this.chat = chat;
    }

    @Override
    public void run() {
        SubTask subTask;

        synchronized (lock) {
            if (!list.isEmpty()) {
                subTask = list.removeFirst();
            } else {
                return;
            }
        }

        if (subTask != null) {
            chat.getDataSource().saveCurrentChannel(subTask.player, subTask.channel);
        }
    }

    @Override
    public int queueSize() {
        return list.size();
    }

    public void queue(KPlayer player) {
        list.addLast(new SubTask(player.getNameLowCase(), player.currentChannel));
    }

    class SubTask {

        String player;
        int channel;

        public SubTask(String player, int channel) {
            this.player = player;
            this.channel = channel;
        }
    }

}
