package com.owentech.DevDrawer.appwidget;

/**
 * Created with IntelliJ IDEA.
 * User: owent
 * Date: 25/01/2013
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.owentech.DevDrawer.R;
import com.owentech.DevDrawer.activities.ClickHandlingActivity;

public class DDWidgetProvider extends AppWidgetProvider {

    public static String PACKAGE_STRING = "default.package";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews widget = getRemoteViews(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, widget);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static RemoteViews getRemoteViews(Context context, int appWidgetId) {
        // Setup the widget, and data source / adapter
        Intent svcIntent = new Intent(context, DDWidgetService.class);

        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        int widgetLayoutResId = sp.getString("theme", "Light").equals("Light") ? R.layout.widget_layout : R.layout.widget_layout_dark;
        RemoteViews widget = new RemoteViews(context.getPackageName(), widgetLayoutResId);
        widget.setRemoteAdapter(R.id.listView, svcIntent);

        Intent clickIntent = new Intent(context, ClickHandlingActivity.class);
        PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        widget.setPendingIntentTemplate(R.id.listView, clickPI);
        return widget;
    }
}