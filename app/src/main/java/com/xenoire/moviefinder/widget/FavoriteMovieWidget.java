package com.xenoire.moviefinder.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.xenoire.moviefinder.DetailActivity;
import com.xenoire.moviefinder.MainActivity;
import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.db.Movie;

import static com.xenoire.moviefinder.DetailActivity.MOVIE_PARCEL;
import static com.xenoire.moviefinder.db.DatabaseContract.CONTENT_URI;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMovieWidget extends AppWidgetProvider {

    public static final String TO_APP = "com.xenoire.moviefinder.TO_APP_ACTION";
    public static final String MOVIE_ID = "com.xenoire.moviefinder.MOVIE_ID";
    public static final String UPDATE_WIDGET_ACTION = "com.xenoire.moviefinder.UPDATE_WIDGET";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, FavoriteWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_widget);
        views.setEmptyView(R.id.stackView, R.id.tv_widget_nodata);
        views.setRemoteAdapter(R.id.stackView, intent);

        Intent toAppIntent = new Intent(context, FavoriteMovieWidget.class);
        toAppIntent.setAction(TO_APP);
        toAppIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, toAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stackView, pendingIntent);
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
    public void onReceive(Context context, Intent intent) {
        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TO_APP)) {
            Log.d(FavoriteMovieWidget.class.getSimpleName(), "OnReceive   TOAPP()");
            Intent goToApp = new Intent(context, DetailActivity.class);
            Movie movie = new Movie();
            movie.setId(intent.getBundleExtra(MOVIE_ID).getInt(MOVIE_ID, 0));
            Log.d("WIDGET", "ID : " + intent.getBundleExtra(MOVIE_ID).getInt(MOVIE_ID, 0));
            goToApp.putExtra(MOVIE_PARCEL, movie);

            Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getId());
            goToApp.setData(uri);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(MainActivity.class);
            taskStackBuilder.addNextIntentWithParentStack(goToApp);
            taskStackBuilder.startActivities();
        }

        if (intent.getAction().equals(UPDATE_WIDGET_ACTION)) {
            final ComponentName cn = new ComponentName(context, FavoriteMovieWidget.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.stackView);
            Log.d("WIDGET", "UPDATE WIDGET ACTION");
        }
        super.onReceive(context, intent);
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

