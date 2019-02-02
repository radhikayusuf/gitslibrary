package id.gits.gitsdesigncomponent.view.gitscalendarview;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mitchellganton on 2017-08-09.
 */

public class DateHelper {

    public static Long ONE_DAY = 86400000L;
    static String APP_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";

    public static String getFormattedDateStringFromString(String dateString) {

        if (dateString == null) {
            return "N/A";
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(APP_DATE_FORMAT);
            Date date = simpleDateFormat.parse(dateString);

            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            cal.setTimeZone(tz);
            cal.setTime(date);

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d");
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma");
            return dateFormat.format(cal.getTime()) + " at " + timeFormat.format(cal.getTime()).toLowerCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Invalid";
    }

    public static String apiToInboxDetailFormat(String date) {
        if (date.contains("T")) {
            String[] arrayDate = date.split("T");

            if (arrayDate.length > 1) {
                Long timestamp = stringToTimeStamp(arrayDate[0] + " " + arrayDate[1], "yyyy-MM-dd hh:mm:ss");
                return getCustomDate(timestamp, "MMM dd") + " at " + getCustomDate(timestamp, "hh:mm a");
            } else {
                return date;
            }

        } else {
            return date;
        }


    }

    public static String getDateFormateFromDateStringForPosts(String dateString) {

        if (dateString == null) {
            return "N/A";
        }

        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(APP_DATE_FORMAT);
            Date date = simpleDateFormat.parse(dateString);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d");
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Invalid";

    }

    public static String getMonthAndYearDateStringFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        return dateFormat.format(date);
    }

    public static String getPresenceLogFormat(Long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(date);
    }

    public static String getCustomDate(Long timeMillis, String format) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getDay(Long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        return dateFormat.format(date);
    }

    public static String getFullDay(Long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        return dateFormat.format(date);
    }

    public static String getMonth(Long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
        return dateFormat.format(date);
    }

    public static String getMonth(int month, boolean fromzero) {
        if (fromzero) {
            return new DateFormatSymbols().getMonths()[month];
        } else {
            return new DateFormatSymbols().getMonths()[month - 1];
        }
    }

    public static int getMonthInt(Long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(dateFormat.format(date));
    }

    public static String getYear(Long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return dateFormat.format(date);
    }

    public static int getYearInt(Long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return Integer.parseInt(dateFormat.format(date));
    }

    public static String getShortMonth(Long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return dateFormat.format(date);
    }

    public static String getTimeAPIFormat(Long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getMaxDay(Long timeMillis) {
        Date date = new Date(timeMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String maxDay = String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        if (maxDay.length() == 1 && calendar.getActualMaximum(Calendar.DAY_OF_MONTH) < 10) {
            maxDay = "0" + maxDay;
        }
        return maxDay;
    }


    public static Date stringToDate(String dateStr, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }


    public static Long stringToTimeStamp(String dateStr, String format) {
        Date date = stringToDate(dateStr, format);
        return date.getTime();
    }
}
