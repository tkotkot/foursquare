package com.tk.foursquaresearch.model;

import org.json.JSONObject;

/**
 * Created by timo on 5/3/2017.
 */

public interface FourSquareModelListener {
    public void onReady();
    public void onSuccess(JSONObject jsonObject);
    public void onLocationError();
    public void onNetworkError();
}
