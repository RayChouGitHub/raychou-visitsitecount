package com.git.hui.boot.redis.site.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by @author yihui in 18:03 19/5/12.
 */
public class DateUtil {

    public static String getToday() {
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        StringBuilder buf = new StringBuilder(8);
        return buf.append(year).append(month < 10 ? "0" : "").append(month).append(day < 10 ? "0" : "").append(day)
                .toString();
    }

    /**
     * 获取当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @return 当前时间
     */
    public static String getNowDateTime(){
        return  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }
}
