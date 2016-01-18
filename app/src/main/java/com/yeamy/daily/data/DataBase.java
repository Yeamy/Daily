package com.yeamy.daily.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yeamy.daily.R;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {
    private static final String T_CONTENT = "t_content";
    private static final String T_TAG = "t_tag";
    private static final String ID = "_id";
    private static final String CREATE = "createTime";
    public static final String START = "startTime";
    public static final String FINISH = "finishTime";
    public static final String COLOR = "color";
    public static final String CONTENT = "content";
    private static final String TAGS = "tags";
    private static final String TAG = "tag";

    private Context context;

    public DataBase(Context context) {
        super(context, "Daily.db", null, 2);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table %s(%s INTEGER primary key autoincrement, %s INTEGER, %s INTEGER, %d INTEGER, %d INTEGER, %s TEXT, %s TEXT)";
        sql = String.format(sql, T_CONTENT, ID, CREATE, START, FINISH, COLOR, CONTENT, TAGS);
        db.execSQL(sql);
        sql = "create table %s(%s INTEGER primary key autoincrement, %s TEXT)";
        sql = String.format(sql, T_TAG, ID, TAG);
        db.execSQL(sql);
//      add sample
        Mission mission = new Mission();
        mission.content = context.getString(R.string.sample_plan_1);
        add(db, mission);
        mission = new Mission();
        mission.content = context.getString(R.string.sample_plan_2);
        add(db, mission);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1: {
                String sql = "alter table %s add %s INTEGER";
                db.execSQL(String.format(sql, T_CONTENT, CREATE));
                db.execSQL(String.format(sql, T_CONTENT, COLOR));
                break;
            }
        }
    }

    private void add(SQLiteDatabase db, Mission mission) {
        ContentValues values = new ContentValues();
        values.put(CREATE, mission.createTime);
        values.put(START, mission.startTime);
        values.put(FINISH, mission.finishTime);
        values.put(COLOR, mission.color);
        values.put(CONTENT, mission.content);
//        values.put(TAGS, mission.tags);
        db.insert(T_CONTENT, null, values);
    }


    public void add(Mission mission) {
        SQLiteDatabase db = getWritableDatabase();
        add(db, mission);
        String sql = "select * from %s where %s == %d limit 1";
        sql = String.format(sql, T_CONTENT, CREATE, mission.createTime);
        Cursor cur = db.rawQuery(sql, null);
        if (cur != null) {
            if (cur.moveToNext()) {
                mission._id = cur.getInt(cur.getColumnIndex(ID));
            }
            cur.close();
        }
        db.close();
    }

    public void update(Mission mission, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        String where = ID + " = " + mission._id;
        db.update(T_CONTENT, values, where, null);
        db.close();
    }

    public void delete(Mission mission) {
        SQLiteDatabase db = getWritableDatabase();
        String where = ID + " = " + mission._id;
        db.delete(T_CONTENT, where, null);
        db.close();
    }

    public ArrayList<Mission> get(long from, int limit) {
        ArrayList<Mission> list = null;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from %s where %s > %d order by %s desc, %s desc limit %d";
        sql = String.format(sql, T_CONTENT, FINISH, from, FINISH, ID, limit);
        Cursor cur = db.rawQuery(sql, null);
        if (cur != null) {
            list = new ArrayList<>(cur.getCount());
            int _id = cur.getColumnIndex(ID);
            int createTime = cur.getColumnIndex(CREATE);
            int startTime = cur.getColumnIndex(START);
            int finishTime = cur.getColumnIndex(FINISH);
            int color = cur.getColumnIndex(COLOR);
            int content = cur.getColumnIndex(CONTENT);
            while (cur.moveToNext()) {
                Mission obj = new Mission();
                obj._id = cur.getInt(_id);
                obj.createTime = cur.getLong(createTime);
                obj.startTime = cur.getLong(startTime);
                obj.finishTime = cur.getLong(finishTime);
                obj.color = cur.getInt(color);
                obj.content = cur.getString(content);
                list.add(obj);
            }
            cur.close();
        }
        db.close();
        return list;
    }

    public String[] getPlans() {
        String[] array = null;
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from %s where %s = %d order by %s desc limit %d";
        sql = String.format(sql, T_CONTENT, FINISH, Long.MAX_VALUE, ID, 20);
        Cursor cur = db.rawQuery(sql, null);
        if (cur != null) {
            array = new String[cur.getCount()];
            int i = 0;
            int content = cur.getColumnIndex(CONTENT);
            while (cur.moveToNext()) {
                array[i++] = cur.getString(content);//.replace('\n', ' ');
            }
            cur.close();
        }
        db.close();
        return array;
    }
}
