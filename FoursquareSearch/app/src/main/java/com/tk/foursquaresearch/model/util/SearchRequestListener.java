package com.tk.foursquaresearch.model.util;

/**
 * Created by timo on 5/3/2017.
 */

public interface SearchRequestListener {
    public void onSuccess(String body);
    public void onError();
}
