package me.kime.kc.Util;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class KCLogFilter implements Filter {

    @Override
    public boolean isLoggable(LogRecord logRecord) {
        return !logRecord.getMessage().contains("issued server command: /login");
    }
}
