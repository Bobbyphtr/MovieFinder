package com.xenoire.moviefinderfavorite;

import com.xenoire.moviefinderfavorite.db.MovieItem;

public interface OnFavoriteItemClicked {
    void onFavoriteListItemClicked(MovieItem movie);
}
