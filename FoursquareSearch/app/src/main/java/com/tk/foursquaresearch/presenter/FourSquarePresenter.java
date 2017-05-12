package com.tk.foursquaresearch.presenter;

import android.content.Context;

import com.tk.foursquaresearch.model.FourSquareModelFactory;
import com.tk.foursquaresearch.view.FourSquareViewInterface;
import com.tk.foursquaresearch.model.FourSquareModel;
import com.tk.foursquaresearch.model.FourSquareModelListener;
import com.tk.foursquaresearch.presenter.util.JsonHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FourSquarePresenter implements FourSquarePresenterInterface, FourSquareModelListener {
    private FourSquarePresenterFactory presenterFactory = null;
    private FourSquareViewInterface viewInterface;
    private FourSquareModel model;
    private Context context;

    public FourSquarePresenter(Context ctx, FourSquarePresenterFactory factory) {
        String id = "<id here>";
        String pwd = "<password here>";
        context = ctx;
        presenterFactory = factory;
        model = presenterFactory.fourSquareModelInstance(presenterFactory.fourSquareModelFactoryInstance(), id, pwd, this);
    }

    public void initialize() {
        model.initialize(context);
    }

    public boolean isReady() {
        return model.isReady();
    }

    public void setView(FourSquareViewInterface view) {
        viewInterface = view;
    }

    public boolean search(String criteria) {
        return model.search(criteria);
    }

    @Override
    public void onReady() {
        if(viewInterface != null) {
            viewInterface.searchReady();
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        if(viewInterface != null) {
            List<HashMap<String,String>> resultList = new ArrayList<HashMap<String,String>>();
            JSONObject response = JsonHelper.getObject(jsonObject, "response");
            JSONArray venues = JsonHelper.getArray(response, "venues");
            for (int i = 0; i < venues.length(); i++) {
                JSONObject venue = JsonHelper.getObjectByIndex(venues, i);
                JSONObject location = JsonHelper.getObject(venue, "location");

                HashMap<String,String> item = new HashMap<String,String>();
                item.put("name", JsonHelper.getString(venue, "name"));
                item.put("address", JsonHelper.getString(location, "address"));
                item.put("distance", JsonHelper.getString(location, "distance"));
                resultList.add(item);
            }
            viewInterface.updateSearchResults(resultList);
        }
    }

    @Override
    public void onLocationError() {
        viewInterface.displayLocationError();
    }

    @Override
    public void onNetworkError() {
        viewInterface.displayNetworkError();
    }
}
