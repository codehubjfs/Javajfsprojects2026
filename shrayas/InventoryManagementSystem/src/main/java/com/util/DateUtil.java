package com.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateUtil {
	
	public static LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}
