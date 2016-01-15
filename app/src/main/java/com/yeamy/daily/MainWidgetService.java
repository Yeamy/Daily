package com.yeamy.daily;

import android.content.Intent;
import android.os.Process;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.yeamy.daily.data.DataBase;

public class MainWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new Factory();
    }

    private class Factory implements RemoteViewsFactory {
        private String[] list;

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            list = new DataBase(MainWidgetService.this).getPlans();
        }

        @Override
        public void onDestroy() {
            list = null;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.length;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= getCount()) {
                return null;
            }
            RemoteViews rv = new RemoteViews(getPackageName(), R.layout.li_widget_main);
            rv.setTextViewText(android.R.id.text1, list[position]);

            Intent li = getPackageManager().getLaunchIntentForPackage(getPackageName());
            rv.setOnClickFillInIntent(android.R.id.text1, li);

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
