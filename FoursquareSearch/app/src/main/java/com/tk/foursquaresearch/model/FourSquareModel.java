package com.tk.foursquaresearch.model;

import android.content.Context;

import com.tk.foursquaresearch.model.util.LocationHandler;
import com.tk.foursquaresearch.model.util.LocationHandlerInterface;
import com.tk.foursquaresearch.model.util.SearchRequest;
import com.tk.foursquaresearch.model.util.SearchRequestHelper;
import com.tk.foursquaresearch.model.util.SearchRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class FourSquareModel implements SearchRequestListener, LocationHandlerInterface{
    private String clientId = "";
    private String clientPwd = "";
    private String searchCriteria = "";
    private SearchRequest searchRequest = null;
    private FourSquareModelListener listener = null;
    private LocationHandler locationHandler = null;
    private FourSquareModelFactoryInterface modelFactory = null;
    private double latitude = 0;
    private double longitude = 0;

    public FourSquareModel(FourSquareModelFactoryInterface factory, String id, String pwd, FourSquareModelListener modelListener) {
        modelFactory = factory;
        clientId = id;
        clientPwd = pwd;
        listener = modelListener;
    }

    public void initialize(Context ctx) {
        locationHandler = modelFactory.locationHandlerInstance(ctx, this);
        locationHandler.initialize();

        if(!locationHandler.isActive()) {
            listener.onLocationError();
        }
    }

    public boolean isReady() {
        return (latitude != 0 || longitude != 0);
    }

    public boolean search(String criteria) {
        boolean ret = false;
        if(criteria != null && !criteria.isEmpty()) {
            if (isReady()) {
                if (searchRequest != null) {
                    searchRequest.cancel();
                    searchRequest = null;
                }
                if (!criteria.equals(searchCriteria) && !criteria.isEmpty()) {
                    searchCriteria = criteria;
                    URL url = SearchRequestHelper.generateRequestUrl(clientId, clientPwd, latitude, longitude, searchCriteria);
                    searchRequest = modelFactory.searchRequestInstance(this);
                    searchRequest.search(url.toString());
                    ret = true;
                }
            }
        }
        return ret;
    }

    @Override
    public void onSearchSuccess(String body) {
        try {
            JSONObject obj = new JSONObject(body);
            listener.onSuccess(obj);
        } catch (JSONException e) {
            listener.onNetworkError();
        }
    }

    @Override
    public void onSearchError() {
        listener.onNetworkError();
    }

    @Override
    public void onLocationUpdate() {
        if(locationHandler.isLocationValid()) {
            latitude = locationHandler.getLatitude();
            longitude = locationHandler.getLongitude();
            listener.onReady();
        }
    }

    @Override
    public void onLocationError() {
        listener.onLocationError();
    }

}
