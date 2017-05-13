package com.tk.foursquaresearch.model;

import android.content.Context;
import android.location.Location;

import com.tk.foursquaresearch.model.util.LocationHandler;
import com.tk.foursquaresearch.model.util.LocationHandlerInterface;
import com.tk.foursquaresearch.model.util.SearchRequest;
import com.tk.foursquaresearch.model.util.SearchRequestListener;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FourSquareModelFactory implements FourSquareModelFactoryInterface {

    public Location locationInstance() {
        return new Location("");
    }

    public LocationHandler locationHandlerInstance(Context ctx, LocationHandlerInterface handlerListener) {
        return new LocationHandler(ctx, this, handlerListener);
    }

    public SearchRequest searchRequestInstance(SearchRequestListener listener) {
        return new SearchRequest(this, listener);
    }

    public OkHttpClient okHttpClientInstance() {
       return new OkHttpClient();
    }

    public Request requestInstance(String url) {
        return new Request.Builder().url(url).build();
    }
}
