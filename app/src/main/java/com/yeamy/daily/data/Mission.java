package com.yeamy.daily.data;

import android.content.Context;
import android.content.res.Resources;

import com.yeamy.daily.R;

import java.io.Serializable;
import java.util.Calendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class Mission implements Serializable {

    public Mission() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        createTime = System.currentTimeMillis();
        startTime = calendar.getTimeInMillis();
        finishTime = Long.MAX_VALUE;
    }

    public Mission(int _id, long startTime, long finishTime, String content) {
        this._id = _id;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.content = content;
    }

    public String finishDate;

    int _id;
    public long startTime;
    public long finishTime;
    long createTime;
    public int color;
    public String content;
//    public String tags;

    public void copy(Mission from) {
        this.startTime = from.startTime;
        this.finishTime = from.finishTime;
        this.content = from.content;
        this.color = from.color;
    }

    public boolean isFinish() {
        return finishTime != Long.MAX_VALUE;
    }

    public static String getDateList(Context context, long timeMillis) {
        if (timeMillis == Long.MAX_VALUE) {
            return context.getString(R.string.time_plan);
        }
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(YEAR);
        calendar.setTimeInMillis(timeMillis);

        Resources res = context.getResources();
        String[] months = res.getStringArray(R.array.months);
        String[] weekdays = res.getStringArray(R.array.weekdays);
        String year = "";
        int y = calendar.get(YEAR);
        if (y != nowYear) {
            year = "\n" + y;
        }
        return context.getString(R.string.date_format,
                months[calendar.get(MONTH)],
                calendar.get(DATE),
                weekdays[calendar.get(DAY_OF_WEEK) - 1],
                year);
    }

    public static String getDateText(Context context, long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);

        StringBuilder sb = new StringBuilder();
        Resources res = context.getResources();
        String[] months = res.getStringArray(R.array.months);
        String[] weekdays = res.getStringArray(R.array.weekdays);
        sb.append(calendar.get(YEAR)).append(' ')
                .append(months[calendar.get(MONTH)]).append(' ')
                .append(calendar.get(DATE)).append(' ')
                .append(weekdays[calendar.get(DAY_OF_WEEK) - 1]);
        return sb.toString();
    }

    public String toString(Context context) {
        StringBuilder sb = new StringBuilder(content);
        sb.append(content).append('\n');
        sb.append(context.getString(R.string.start_time)).append(' ').append(getDateText(context, startTime));
        if (finishTime != Long.MAX_VALUE) {
            sb.append('\n');
            sb.append(context.getString(R.string.finish_time)).append(' ').append(getDateText(context, finishTime));
        }
        return sb.toString();
    }
}
