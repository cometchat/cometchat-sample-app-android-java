package com.inscripts.cometchatpulse.demo.Utils;

import android.content.Context;
import android.text.format.DateFormat;

import com.inscripts.cometchatpulse.demo.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    public static String getTimeStringFromTimestamp(long timestamp, String format) {

        java.util.Date dt = new java.util.Date(timestamp * 1000);

        Timestamp time = new Timestamp(dt.getTime());
        Logger.debug("timestamp", time + "rime" + dt.getDate() + " inside");
        String str = getMessageTime(time.toString(), format);
        Logger.debug("timestamp", str + dt.getTime() + " inside");
        return str;
    }

    public static String getMessageTime(String time, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(time);
        String str = format.format(timestamp);
        return str;
    }

    public static String convertTimeStampToDurationTime(long var0) {
        long var2 = var0 / 1000L;
        long var4 = var2 / 60L % 60L;
        long var6 = var2 / 60L / 60L % 24L;
            return var6 == 0L ? String.format(Locale.getDefault(), "%02d:%02d", var4, var2 % 60L) : String.format(Locale.getDefault(), "%02d:%02d:%02d", var6, var4, var2 % 60L);
    }

    public static String getDateId(long var0) {
        Calendar var2 = Calendar.getInstance(Locale.ENGLISH);
        var2.setTimeInMillis(var0);
        String var3 = DateFormat.format("ddMMyyyy", var2).toString();
        return var3;
    }

    public static String getCustomizeDate(long time) {
        String[] monthNames = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September"
                , "October", "November", "December"};

        String date = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        date = day + " " + monthNames[month] + ", " + year;

        return date;
    }


    public static String getLastSeenDate(long timeStamp, Context context) {

        String lastSeenTime = new java.text.SimpleDateFormat("HH:mm a").format(new java.util.Date(timeStamp));
        String lastSeenDate = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(timeStamp));

        long currentTimeStamp = System.currentTimeMillis();

        long diffTimeStamp = (currentTimeStamp - timeStamp)/1000;

        if (diffTimeStamp < 24 * 60 * 60) {

            return context.getString(R.string.today_last_seen) + " " + lastSeenTime;

        } else if (diffTimeStamp < 48 * 60 * 60) {

            return context.getString(R.string.yesterday_last_seen) + " " + lastSeenTime;
        } else {
            return "Last seen at " + lastSeenDate + " on " + lastSeenTime;
        }

    }
}
