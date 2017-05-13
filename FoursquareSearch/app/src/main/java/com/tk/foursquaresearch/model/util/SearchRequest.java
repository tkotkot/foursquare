package com.tk.foursquaresearch.model.util;

import android.util.Log;

import com.tk.foursquaresearch.model.FourSquareModelFactoryInterface;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchRequest {
    private FourSquareModelFactoryInterface modelFactory = null;
    private OkHttpClient client;
    private Call call;
    private SearchRequestListener listener;

    public void search(String url) {
        client = modelFactory.okHttpClientInstance();
        Request request = modelFactory.requestInstance(url);

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(listener != null) {
                    listener.onSearchError();
                }
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        if(listener != null) {
                            if(body != null && !body.isEmpty()) {
                                listener.onSearchSuccess(body);
                            } else {
                                listener.onSearchError();
                            }
                        }
                    } else {
                        if(listener != null) {
                            listener.onSearchError();
                        }
                    }
                } catch (Exception e) {
                    if(listener != null) {
                        listener.onSearchError();
                    }
                }
            }
        });
    }

    public void cancel() {
        if(call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    public SearchRequest(FourSquareModelFactoryInterface factory, SearchRequestListener requestListener) {
        modelFactory = factory;
        listener = requestListener;
    }
}
