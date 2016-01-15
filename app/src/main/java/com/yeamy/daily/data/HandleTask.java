package com.yeamy.daily.data;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

import com.yeamy.daily.MainWidgetProvider;

public class HandleTask {

    public static void add(final Context context, final Mission mission) {
        AsyncTaskCompat.executeParallel(new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(context);
                db.add(mission);
                updateWidget(context);
                return null;
            }
        });
    }

    public static void del(final Context context, final Mission mission) {
        AsyncTaskCompat.executeParallel(new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(context);
                db.delete(mission);
                updateWidget(context);
                return null;
            }
        });
    }

    public static void update(final Context context, final Mission mission, final ContentValues values) {
        AsyncTaskCompat.executeParallel(new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(context);
                db.update(mission, values);
                updateWidget(context);
                return null;
            }
        });
    }

    public static void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, MainWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, android.R.id.list);
    }

}
