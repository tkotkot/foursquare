package com.tk.foursquaresearch.model.util;

/**
 * Created by timo on 5/3/2017.
 */

public interface SearchRequestListener {
    public void onSearchSuccess(String body);
    public void onSearchError();
}
