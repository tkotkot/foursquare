package com.tk.foursquaresearch.model;

import android.content.Context;

import com.tk.foursquaresearch.model.FourSquareModelFactoryInterface;
import com.tk.foursquaresearch.model.util.LocationHandler;
import com.tk.foursquaresearch.model.util.SearchRequest;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FourSquareModelTest {

    Context mockContext = null;
    FourSquareModelFactoryInterface mockFactory = null;
    FourSquareModelListener mockListener = null;
    LocationHandler mockLocationHandler = null;
    SearchRequest mockSearchRequest = null;

    @Before
    public void initializeTest() {
        mockContext = mock(Context.class);
        mockFactory = mock(FourSquareModelFactoryInterface.class);
        mockListener = mock(FourSquareModelListener.class);
        mockLocationHandler = mock(LocationHandler.class);
        mockSearchRequest = mock(SearchRequest.class);
    }

    @Test
    public void testConstruct()  {
        FourSquareModel model = new FourSquareModel(mockFactory,"aa","bb", mockListener);
        assertEquals(model.isReady(), false);
    }

    @Test
    public void testInitializeError()  {

        FourSquareModel model = new FourSquareModel(mockFactory,"aa","bb", mockListener);

        when(mockFactory.locationHandlerInstance(mockContext, model)).thenReturn(mockLocationHandler);
        when(mockLocationHandler.isActive()).thenReturn(false);
        model.initialize(mockContext);
        assertEquals(model.isReady(), false);
        verify(mockListener, times(1)).onLocationError();
    }

    @Test
    public void testInitializeSuccess()  {

        FourSquareModel model = new FourSquareModel(mockFactory,"aa","bb", mockListener);

        when(mockFactory.locationHandlerInstance(mockContext, model)).thenReturn(mockLocationHandler);
        when(mockLocationHandler.isActive()).thenReturn(true);

        model.initialize(mockContext);
        assertEquals(model.isReady(), false);
        verify(mockListener, never()).onLocationError();
    }

    @Test
    public void testSearchNotReady()  {

        FourSquareModel model = new FourSquareModel(mockFactory,"aa","bb", mockListener);

        when(mockFactory.locationHandlerInstance(mockContext, model)).thenReturn(mockLocationHandler);
        when(mockFactory.searchRequestInstance(model)).thenReturn(mockSearchRequest);
        when(mockLocationHandler.isActive()).thenReturn(true);

        model.initialize(mockContext);
        boolean success = model.search("test");
        assertEquals(success, false);
    }

    @Test
    public void testSearchReady()  {
        String url = "https://api.foursquare.com/v2/venues/search?client_id=aa&client_secret=bb&v=20170501&limit=10&ll=10.100000,5.200000&query=test";
        FourSquareModel model = new FourSquareModel(mockFactory,"aa","bb", mockListener);

        when(mockFactory.locationHandlerInstance(mockContext, model)).thenReturn(mockLocationHandler);
        when(mockFactory.searchRequestInstance(model)).thenReturn(mockSearchRequest);
        when(mockLocationHandler.isActive()).thenReturn(true);
        when(mockLocationHandler.isLocationValid()).thenReturn(true);
        when(mockLocationHandler.getLatitude()).thenReturn(10.1d);
        when(mockLocationHandler.getLongitude()).thenReturn(5.2d);

        model.initialize(mockContext);
        model.onLocationUpdate();
        verify(mockListener, times(1)).onReady();

        boolean success = model.search("test");
        assertEquals(success, true);
        verify(mockSearchRequest, times(1)).search(url);
    }

    @Test
    public void testSearchTimesTwo()  {
        String url = "https://api.foursquare.com/v2/venues/search?client_id=aa&client_secret=bb&v=20170501&limit=10&ll=10.100000,5.200000&query=test";
        FourSquareModel model = new FourSquareModel(mockFactory,"aa","bb", mockListener);

        when(mockFactory.locationHandlerInstance(mockContext, model)).thenReturn(mockLocationHandler);
        when(mockFactory.searchRequestInstance(model)).thenReturn(mockSearchRequest);
        when(mockLocationHandler.isActive()).thenReturn(true);
        when(mockLocationHandler.isLocationValid()).thenReturn(true);
        when(mockLocationHandler.getLatitude()).thenReturn(10.1d);
        when(mockLocationHandler.getLongitude()).thenReturn(5.2d);

        model.initialize(mockContext);
        model.onLocationUpdate();
        boolean success = model.search("test");
        assertEquals(success, true);
        success = model.search("test2");
        assertEquals(success, true);
        verify(mockSearchRequest, times(1)).cancel();
        verify(mockSearchRequest, times(2)).search(any(String.class));
    }

    @Test
    public void testOnLocationError()  {

        FourSquareModel model = new FourSquareModel(mockFactory,"aa","bb", mockListener);

        when(mockFactory.locationHandlerInstance(mockContext, model)).thenReturn(mockLocationHandler);
        when(mockFactory.searchRequestInstance(model)).thenReturn(mockSearchRequest);
        when(mockLocationHandler.isActive()).thenReturn(true);

        model.initialize(mockContext);
        model.onLocationError();
        verify(mockListener, times(1)).onLocationError();
    }

    @Test
    public void testOnSearchError()  {

        FourSquareModel model = new FourSquareModel(mockFactory,"aa","bb", mockListener);

        when(mockFactory.locationHandlerInstance(mockContext, model)).thenReturn(mockLocationHandler);
        when(mockFactory.searchRequestInstance(model)).thenReturn(mockSearchRequest);
        when(mockLocationHandler.isActive()).thenReturn(true);

        model.initialize(mockContext);
        model.onSearchError();
        verify(mockListener, times(1)).onNetworkError();
    }
}
