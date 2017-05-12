package com.tk.foursquaresearch.presenter;


import android.content.Context;

import com.tk.foursquaresearch.model.FourSquareModel;
import com.tk.foursquaresearch.model.FourSquareModelFactory;
import com.tk.foursquaresearch.model.FourSquareModelFactoryInterface;
import com.tk.foursquaresearch.model.FourSquareModelListener;

public class FourSquarePresenterFactory {
    private static FourSquarePresenterInterface presenter = null;

    public static FourSquarePresenterInterface getPresenter(Context context) {
        if(presenter == null) {
            presenter = new FourSquarePresenter(context, new FourSquarePresenterFactory());
        }
        return presenter;
    };

    public FourSquareModelFactory fourSquareModelFactoryInstance() {
        return new FourSquareModelFactory();
    }

    public FourSquareModel fourSquareModelInstance(FourSquareModelFactoryInterface factory, String id, String pwd, FourSquareModelListener modelListener) {
        return new FourSquareModel(factory, id, pwd, modelListener);
    }

    public static void setPresenterForTest(FourSquarePresenterInterface presenterInterface) {
        presenter = presenterInterface;
    }
}
