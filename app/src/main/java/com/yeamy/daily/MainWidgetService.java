package com.yeamy.daily;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.yeamy.daily.data.DataBase;
import com.yeamy.daily.data.SimpleMission;

public class MainWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new Factory();
    }

    private class Factory implements RemoteViewsFactory {
        private SimpleMission[] list;
        private SparseArray<Bitmap> colors;

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            list = new DataBase(MainWidgetService.this).getPlans();
            colors = new SparseArray<>(7);
        }

        @Override
        public void onDestroy() {
            list = null;
            colors = null;
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
            SimpleMission item = list[position];
            rv.setTextViewText(R.id.content, item.content);
            final int color = item.color;
            Bitmap bitmap = colors.get(color);
            if (bitmap == null) {
                Bitmap.Config config = (color == 0) ? Bitmap.Config.ALPHA_8 : Bitmap.Config.RGB_565;
                bitmap = Bitmap.createBitmap(new int[]{color}, 1, 1, config);
                colors.put(color, bitmap);
            }
            rv.setImageViewBitmap(R.id.color, bitmap);

            Intent li = getPackageManager().getLaunchIntentForPackage(getPackageName());
            rv.setOnClickFillInIntent(R.id.content, li);

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
