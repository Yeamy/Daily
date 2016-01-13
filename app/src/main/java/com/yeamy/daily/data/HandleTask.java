package com.yeamy.daily.data;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

public class HandleTask {

    public static void add(final Context context, final Mission mission) {
        AsyncTaskCompat.executeParallel(new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(context);
                db.add(mission);
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
                return null;
            }
        });
    }

}
