package com.tk.foursquaresearch.model.util;

import android.util.Log;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchRequest {
    private OkHttpClient client;
    private Call call;
    private SearchRequestListener listener;

    public void search(String url) {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(listener != null) {
                    listener.onError();
                }
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        if(listener != null) {
                            listener.onSuccess(body);
                        }
                    } else {
                        if(listener != null) {
                            listener.onError();
                        }
                    }
                } catch (Exception e) {
                    if(listener != null) {
                        listener.onError();
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

    public SearchRequest(SearchRequestListener requestListener) {
        listener = requestListener;
    }
}
