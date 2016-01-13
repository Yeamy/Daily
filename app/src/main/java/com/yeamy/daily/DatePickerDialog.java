package com.yeamy.daily;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerDialog extends AlertDialog.Builder implements DialogInterface.OnClickListener {
    private DatePicker picker;
    private OnDateSetListener listener;
    private int id;

    public DatePickerDialog(Context context, int id, long from, boolean showNeutralButton) {
        super(context, R.style.AppTheme_Dialog);
        setPositiveButton(android.R.string.ok, this);
        if (showNeutralButton && from != Long.MAX_VALUE) {
            setNeutralButton(R.string.datePicker_btn_unFinished, this);
        }
        setNegativeButton(android.R.string.cancel, this);

        this.id = id;
        picker = new DatePicker(context);
        picker.setCalendarViewShown(Build.VERSION.SDK_INT >= 21);
        setView(picker);
    }

    public void setListener(OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (listener == null) {
            return;
        }
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                int year = picker.getYear();
                int month = picker.getMonth();
                int date = picker.getDayOfMonth();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, date);
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                StringBuilder sb = new StringBuilder();
                String[] months = getContext().getResources().getStringArray(R.array.months);
                String[] weekdays = getContext().getResources().getStringArray(R.array.weekdays);
                sb.append(year).append(' ')
                        .append(months[month]).append(' ')
                        .append(date).append(' ')
                        .append(weekdays[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
                listener.onDateSet(id, true, calendar.getTimeInMillis(), sb.toString());
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                listener.onDateSet(id, false, 0L, null);
        }
    }

    public interface OnDateSetListener {

        void onDateSet(int id, boolean enable, long timeMillis, String date);
    }

}
