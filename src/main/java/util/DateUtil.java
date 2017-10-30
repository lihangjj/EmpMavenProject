package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static Date getEglishUtilDate(String dateString) {

        try {
            return new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date getChinaUtilDate(String dateString) {

        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date getChinaUtilDate(java.sql.Date sqlDate) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return simpleDateFormat.parse(simpleDateFormat.format(sqlDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date(sqlDate.getTime());
    }

    public static String getSimpleDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getSimpleDatehhmmss(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return simpleDateFormat.format(date);
    }
}
