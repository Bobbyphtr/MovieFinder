package com.xenoire.moviefinder.alarms;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.xenoire.moviefinder.BuildConfig;
import com.xenoire.moviefinder.MainActivity;
import com.xenoire.moviefinder.R;
import com.xenoire.moviefinder.db.Movie;
import com.xenoire.moviefinder.fragments.NowPlayingFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "MovieFinderReceiver";

    public static final String NOTIF_TYPE = "notif_type";
    public static final String NOTIF_TYPE_DAILY_REMINDER = "daily_reminder";
    public static final String NOTIF_TYPE_RELEASED_TODAY = "released_today";

    private final int NOTIF_ID_DAILY_REMINDER = 50;
    private final int NOTIF_ID_RELEASED_TODAY = 51;

    private final String CHANNEL_ID_DR = "DailyReminder";
    private final String CHANNEL_NAME_DR = "Daily Reminder";
    private final String CHANNEL_ID_RT = "ReleasedToday";
    private final String CHANNEL_NAME_RT = "Released Today";

    private Context context;

    public AlarmReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(NOTIF_TYPE);
        this.context = context;
        Log.d(TAG, "        ONRECEIVE");
        if(type.equalsIgnoreCase(NOTIF_TYPE_DAILY_REMINDER)){
            showNotification(context, NOTIF_ID_DAILY_REMINDER, type, null);
        } else {
            setReleasedTodayAlarm();
        }
    }
    private void showNotification(Context context, int notifId, String type, @Nullable String movieTitle) {
        NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (type.equals(NOTIF_TYPE_RELEASED_TODAY) && TextUtils.isEmpty(movieTitle)) {
            //Released Today
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder = new NotificationCompat.Builder(context, CHANNEL_ID_RT)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                    .setContentTitle(movieTitle)
                    .setContentText(context.getResources().getString(R.string.movie_released_today, movieTitle))
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);
            //Setting for android O
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID_RT, CHANNEL_NAME_RT, NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{0, 500, 0, 500});
                builder.setChannelId(CHANNEL_ID_RT);
                if (mgr != null) {
                    mgr.createNotificationChannel(channel);
                }
            }
            Log.d(TAG, "SHOW NOTIFICATION () RELEASED TODAY");
        } else {
            //Daily Reminder
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder = new NotificationCompat.Builder(context, CHANNEL_ID_DR)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                    .setContentTitle(context.getResources().getString(R.string.movie_finder))
                    .setContentText(context.getResources().getString(R.string.movie_finder_is_missing_you))
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);
            //Setting for android O
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID_DR, CHANNEL_NAME_DR, NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{0, 500, 0, 500});
                builder.setChannelId(CHANNEL_ID_DR);
                if (mgr != null) {
                    mgr.createNotificationChannel(channel);
                }
            }
            Log.d(TAG, "SHOW NOTIFICATION () DAILYREMINDER");
        }
        if (mgr != null) {
            mgr.notify(notifId, builder.build());
            Log.d(TAG, "NOTIFIED()");
        }
    }

    public void setAlarm(Context context, String type) {
        PendingIntent pendingIntent = null;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(NOTIF_TYPE, NOTIF_TYPE_DAILY_REMINDER);
        Calendar calendar = Calendar.getInstance();
        if(type.equalsIgnoreCase(NOTIF_TYPE_DAILY_REMINDER)){
            calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Log.d(TAG, "DailyReminder millis " + calendar.getTimeInMillis() + " date and time " + calendar.getTime().toString());
            pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_DAILY_REMINDER, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }else if (type.equalsIgnoreCase(NOTIF_TYPE_RELEASED_TODAY)){
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Log.d(TAG, "Released Today millis " + calendar.getTimeInMillis() + " date and time " + calendar.getTime().toString());
            pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_RELEASED_TODAY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Log.d(TAG, "ALARM DAILY NOTIFICATION SET UP");
    }

    private void setReleasedTodayAlarm() {
        SyncHttpClient client = new SyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US";
        Date today = Calendar.getInstance().getTime();
        final ArrayList<Movie> movies = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final String todayFormatted = df.format(today);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject resultObj = new JSONObject(result);
                    JSONArray list = resultObj.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        Movie myMovie = new Movie(list.getJSONObject(i));
                        if (myMovie.getReleaseDate().equalsIgnoreCase(todayFormatted)) {
                            movies.add(myMovie);
                        }
                    }
                    for (Movie movie : movies) {
                        showNotification(context, movie.getId(), NOTIF_TYPE_RELEASED_TODAY, movie.getTitle());
                    }
                    Log.w(NowPlayingFragment.TAG, "Success on fetch");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "Fail to fetch!");
            }
        });
    }
}
