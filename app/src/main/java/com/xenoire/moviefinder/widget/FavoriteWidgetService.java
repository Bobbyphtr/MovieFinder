package com.xenoire.moviefinder.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class FavoriteWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("WidgetFavService", "OnGetViewFactory");
        return new FavoriteRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
