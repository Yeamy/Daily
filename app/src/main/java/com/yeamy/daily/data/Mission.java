package com.yeamy.daily.data;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import com.yeamy.daily.R;

import java.util.Calendar;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class Mission implements Parcelable {

    int _id;
    public long startTime;
    public long finishTime;
    long createTime;
    public int color;
    public String content;
    //    public String tags;

    public CharSequence finishDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[]{_id, color});
        dest.writeLongArray(new long[]{startTime, finishTime, createTime});
        dest.writeString(content);
    }

    public static final Parcelable.Creator<Mission> CREATOR = new Parcelable.Creator<Mission>() {

        @Override
        public Mission createFromParcel(Parcel source) {
            int[] ints = new int[2];
            source.readIntArray(ints);
            long[] longs = new long[3];
            source.readLongArray(longs);
            return new Mission(ints[0], ints[1], longs[0], longs[1], longs[2], source.readString());
        }

        @Override
        public Mission[] newArray(int size) {
            return new Mission[0];
        }
    };

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

    public Mission(int _id, int color, long startTime, long finishTime, long createTime, String content) {
        this._id = _id;
        this.color = color;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.createTime = createTime;
        this.content = content;
    }

    public void copy(Mission from) {
        this.startTime = from.startTime;
        this.finishTime = from.finishTime;
        this.content = from.content;
        this.color = from.color;
    }

    public boolean isFinish() {
        return finishTime != Long.MAX_VALUE;
    }

    public static CharSequence getDateList(Context context, long timeMillis) {
        if (timeMillis == Long.MAX_VALUE) {
            SpannableString sp = new SpannableString(context.getString(R.string.time_plan));
            sp.setSpan(new RelativeSizeSpan(1.07f), 0, sp.length(), 0);
            return sp;
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
