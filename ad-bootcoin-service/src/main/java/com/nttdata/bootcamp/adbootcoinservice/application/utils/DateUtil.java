package com.nttdata.bootcamp.adbootcoinservice.application.utils;

import java.util.Calendar;
import java.util.Date;


/**
 *
 * @since 2022
 */
public class DateUtil {

    public static  Date setStartDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getStartOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getStartCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        calendar.set(Calendar.HOUR_OF_DAY,
          Calendar.getInstance().getMaximum(Calendar.HOUR_OF_DAY));

        calendar.set(Calendar.MINUTE,
          Calendar.getInstance().getMaximum(Calendar.MINUTE));

        calendar.set(Calendar.SECOND,
          Calendar.getInstance().getMaximum(Calendar.SECOND));

        calendar.set(Calendar.MILLISECOND,
          Calendar.getInstance().getMaximum(Calendar.MILLISECOND));

        return calendar.getTime();
    }



    public static Integer getCurrentDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getDateStartByDayOfMonth(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Date getDateEndByDayOfMonth(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, day);

        calendar.set(Calendar.HOUR_OF_DAY,
          Calendar.getInstance().getMaximum(Calendar.HOUR_OF_DAY));

        calendar.set(Calendar.MINUTE,
          Calendar.getInstance().getMaximum(Calendar.MINUTE));

        calendar.set(Calendar.SECOND,
          Calendar.getInstance().getMaximum(Calendar.SECOND));

        calendar.set(Calendar.MILLISECOND,
          Calendar.getInstance().getMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }
}
