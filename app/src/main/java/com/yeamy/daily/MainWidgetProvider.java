package com.yeamy.daily;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MainWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive" + intent.getAction());
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        // Construct the RemoteViews object
        String packageName = context.getPackageName();
        Intent li = context.getPackageManager().getLaunchIntentForPackage(packageName);
        PendingIntent pi = PendingIntent.getActivity(context, 0, li, 0);

        RemoteViews views = new RemoteViews(packageName, R.layout.widget_main);
        views.setOnClickPendingIntent(android.R.id.title, pi);

        views.setRemoteAdapter(android.R.id.list, new Intent(context, MainWidgetService.class));
        views.setPendingIntentTemplate(android.R.id.list, pi);

        for (int widgetId : appWidgetIds) {
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(widgetId, views);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

