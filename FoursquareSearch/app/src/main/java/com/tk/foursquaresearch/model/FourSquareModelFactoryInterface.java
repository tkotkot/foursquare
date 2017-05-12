package com.tk.foursquaresearch.model;

import android.content.Context;
import android.location.Location;

import com.tk.foursquaresearch.model.util.LocationHandler;
import com.tk.foursquaresearch.model.util.LocationHandlerInterface;
import com.tk.foursquaresearch.model.util.SearchRequest;
import com.tk.foursquaresearch.model.util.SearchRequestListener;

public interface FourSquareModelFactoryInterface {

    Location locationInstance();
    LocationHandler locationHandlerInstance(Context ctx, LocationHandlerInterface handlerListener);
    SearchRequest searchRequestInstance(SearchRequestListener listener);
}
