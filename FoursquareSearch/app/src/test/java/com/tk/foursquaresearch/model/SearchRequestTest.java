package com.tk.foursquaresearch.model;

import com.tk.foursquaresearch.model.util.SearchRequest;
import com.tk.foursquaresearch.model.util.SearchRequestListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchRequestTest {

    FourSquareModelFactoryInterface mockFactory = null;
    SearchRequestListener mockListener = null;
    OkHttpClient mockClient = null;
    Request mockRequest = null;
    Response mockResponse = null;
    ResponseBody mockBody = null;
    Call mockCall = null;

    @Before
    public void initializeTest() {
        mockFactory = mock(FourSquareModelFactoryInterface.class);
        mockListener = mock(SearchRequestListener.class);
        mockClient = mock(OkHttpClient.class);
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        mockBody = mock(ResponseBody.class);
        mockCall = mock(Call.class);
    }

    @Test
    public void testConstruct()  {
        when(mockFactory.okHttpClientInstance()).thenReturn(mockClient);
        when(mockFactory.requestInstance(anyString())).thenReturn(mockRequest);

        SearchRequest searchRequest = new SearchRequest(mockFactory, mockListener);

        verify(mockListener, never()).onSearchSuccess(anyString());
        verify(mockListener, never()).onSearchError();
        verify(mockClient, never()).newCall(mockRequest);
    }

    @Test
    public void testSearchSuccess()  {
        when(mockFactory.okHttpClientInstance()).thenReturn(mockClient);
        when(mockFactory.requestInstance("test_url")).thenReturn(mockRequest);
        when(mockClient.newCall(mockRequest)).thenReturn(mockCall);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockBody);
        try {
            when(mockBody.string()).thenReturn("{'a':'b','num':1}");
        } catch(Exception e) {
        }

        SearchRequest searchRequest = new SearchRequest(mockFactory, mockListener);
        searchRequest.search("test_url");

        verify(mockClient, times(1)).newCall(mockRequest);
        ArgumentCaptor<Callback> argument = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall, times(1)).enqueue(argument.capture());
        Callback callback = argument.getValue();
        verify(mockListener, never()).onSearchSuccess(anyString());
        verify(mockListener, never()).onSearchError();
        try {
            callback.onResponse(mockCall, mockResponse);
        } catch(Exception e) {
        }
        verify(mockResponse, times(1)).isSuccessful();
        verify(mockResponse, times(1)).body();
        verify(mockListener, times(1)).onSearchSuccess("{'a':'b','num':1}");
    }

    @Test
    public void testSearchError()  {
        when(mockFactory.okHttpClientInstance()).thenReturn(mockClient);
        when(mockFactory.requestInstance("test_url")).thenReturn(mockRequest);
        when(mockClient.newCall(mockRequest)).thenReturn(mockCall);
        when(mockResponse.isSuccessful()).thenReturn(false);

        SearchRequest searchRequest = new SearchRequest(mockFactory, mockListener);
        searchRequest.search("test_url");
        verify(mockClient, times(1)).newCall(mockRequest);
        ArgumentCaptor<Callback> argument = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall, times(1)).enqueue(argument.capture());
        Callback callback = argument.getValue();
        verify(mockListener, never()).onSearchSuccess(anyString());
        verify(mockListener, never()).onSearchError();
        try {
            callback.onResponse(mockCall, mockResponse);
        } catch(Exception e) {
        }
        verify(mockResponse, times(1)).isSuccessful();
        verify(mockResponse, never()).body();
        verify(mockListener, never()).onSearchSuccess(anyString());
        verify(mockListener, times(1)).onSearchError();
    }

    @Test
    public void testSearchError2()  {
        when(mockFactory.okHttpClientInstance()).thenReturn(mockClient);
        when(mockFactory.requestInstance("test_url")).thenReturn(mockRequest);
        when(mockClient.newCall(mockRequest)).thenReturn(mockCall);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockBody);
        try {
            when(mockBody.string()).thenReturn(null);
        } catch(Exception e) {
        }

        SearchRequest searchRequest = new SearchRequest(mockFactory, mockListener);
        searchRequest.search("test_url");
        verify(mockClient, times(1)).newCall(mockRequest);
        ArgumentCaptor<Callback> argument = ArgumentCaptor.forClass(Callback.class);
        verify(mockCall, times(1)).enqueue(argument.capture());
        Callback callback = argument.getValue();
        verify(mockListener, never()).onSearchSuccess(anyString());
        verify(mockListener, never()).onSearchError();
        try {
            callback.onResponse(mockCall, mockResponse);
        } catch(Exception e) {
        }
        verify(mockResponse, times(1)).isSuccessful();
        verify(mockResponse, times(1)).body();
        verify(mockListener, never()).onSearchSuccess(anyString());
        verify(mockListener, times(1)).onSearchError();
    }
}
