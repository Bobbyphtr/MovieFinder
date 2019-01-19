package com.xenoire.moviefinder.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.xenoire.moviefinder.MainActivity;
import com.xenoire.moviefinder.R;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMovieWidget extends AppWidgetProvider {

    public static final String OPEN_APP_ACTION = "com.xenoire.moviefinder.OPEN_APP_ACTION";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, FavoriteWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_widget);
        views.setRemoteAdapter(R.id.stackView, intent);
        views.setEmptyView(R.id.stackView, R.id.tv_widget_nodata);
        Log.d("FavoriteWidget", "   views.setEmptyView()");
        Intent openAppIntent = new Intent(context, FavoriteMovieWidget.class);
        openAppIntent.setAction(OPEN_APP_ACTION);
        Log.d("FavoriteWidget", "   openAppIntent");
        PendingIntent openAppPendingIntent = PendingIntent.getBroadcast(context, 0, openAppIntent, 0);
        views.setOnClickPendingIntent(appWidgetId, openAppPendingIntent);
        Log.d("FavoriteWidget", "   updateAppWidget()");
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

    @Override
    public void onReceive(Context context, Intent intent) {
//        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//        if(intent.getAction().equals(OPEN_APP_ACTION)){
//            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
//            Intent openApp = new Intent(context.getApplicationContext(), MainActivity.class);
//            context.startActivity(openApp);
//        }
        super.onReceive(context, intent);
    }
}

