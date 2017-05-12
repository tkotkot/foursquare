package com.tk.foursquaresearch.presenter;

import android.content.Context;

import com.tk.foursquaresearch.model.FourSquareModel;
import com.tk.foursquaresearch.model.FourSquareModelFactory;
import com.tk.foursquaresearch.model.FourSquareModelFactoryInterface;
import com.tk.foursquaresearch.model.FourSquareModelListener;
import com.tk.foursquaresearch.presenter.util.JsonHelper;
import com.tk.foursquaresearch.view.FourSquareViewInterface;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FourSquarePresenterTest {
    @Test
    public void testConstruct()  {
        Context mockContext = mock(Context.class);
        FourSquarePresenterFactory mockFactory = mock(FourSquarePresenterFactory.class);
        FourSquareModel mockModel = mock(FourSquareModel.class);
        FourSquareModelFactory mockModelFactory = mock(FourSquareModelFactory.class);

        when(mockFactory.fourSquareModelInstance(any(FourSquareModelFactoryInterface.class),anyString(),anyString(),any(FourSquareModelListener.class))).thenReturn(mockModel);
        when(mockFactory.fourSquareModelFactoryInstance()).thenReturn(mockModelFactory);
        when(mockModel.isReady()).thenReturn(false);

        FourSquarePresenter presenter = new FourSquarePresenter(mockContext, mockFactory);
        assertEquals(presenter.isReady(), false);
    }

    @Test
    public void testSearch()  {
        Context mockContext = mock(Context.class);
        FourSquarePresenterFactory mockFactory = mock(FourSquarePresenterFactory.class);
        FourSquareModel mockModel = mock(FourSquareModel.class);
        FourSquareModelFactory mockModelFactory = mock(FourSquareModelFactory.class);

        when(mockFactory.fourSquareModelInstance(any(FourSquareModelFactoryInterface.class),anyString(),anyString(),any(FourSquareModelListener.class))).thenReturn(mockModel);
        when(mockFactory.fourSquareModelFactoryInstance()).thenReturn(mockModelFactory);
        when(mockModel.isReady()).thenReturn(true);

        FourSquarePresenter presenter = new FourSquarePresenter(mockContext, mockFactory);
        assertEquals(presenter.isReady(), true);

        presenter.search("test criteria");
        verify(mockModel, times(1)).search("test criteria");
    }

    @Test
    public void testOnSuccessEmpty()  {
        Context mockContext = mock(Context.class);
        FourSquarePresenterFactory mockFactory = mock(FourSquarePresenterFactory.class);
        FourSquareModel mockModel = mock(FourSquareModel.class);
        FourSquareModelFactory mockModelFactory = mock(FourSquareModelFactory.class);
        FourSquareViewInterface mockView = mock(FourSquareViewInterface.class);
        mock(JsonHelper.class);
        JSONObject mockJsonObject = mock(JSONObject.class);
        JSONArray mockJsonArray = mock(JSONArray.class);

        when(mockFactory.fourSquareModelInstance(any(FourSquareModelFactoryInterface.class),anyString(),anyString(),any(FourSquareModelListener.class))).thenReturn(mockModel);
        when(mockFactory.fourSquareModelFactoryInstance()).thenReturn(mockModelFactory);
        when(mockModel.isReady()).thenReturn(true);
        try {
            when(mockJsonObject.getJSONObject(anyString())).thenReturn(mockJsonObject);
            when(mockJsonObject.getJSONArray(anyString())).thenReturn(mockJsonArray);
        } catch(Exception e) {
        }
        when(mockJsonArray.length()).thenReturn(0);

        FourSquarePresenter presenter = new FourSquarePresenter(mockContext, mockFactory);
        presenter.setView(mockView);
        assertEquals(presenter.isReady(), true);

        presenter.onSuccess(mockJsonObject);
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(mockView, times(1)).updateSearchResults(argument.capture());
        assertEquals(0, argument.getValue().size());
    }

    @Test
    public void testOnSuccessOneHit()  {
        Context mockContext = mock(Context.class);
        FourSquarePresenterFactory mockFactory = mock(FourSquarePresenterFactory.class);
        FourSquareModel mockModel = mock(FourSquareModel.class);
        FourSquareModelFactory mockModelFactory = mock(FourSquareModelFactory.class);
        FourSquareViewInterface mockView = mock(FourSquareViewInterface.class);
        mock(JsonHelper.class);
        JSONObject mockJsonObject = mock(JSONObject.class);
        JSONArray mockJsonArray = mock(JSONArray.class);
        JSONObject mockJsonHit1Object = mock(JSONObject.class);

        when(mockFactory.fourSquareModelInstance(any(FourSquareModelFactoryInterface.class),anyString(),anyString(),any(FourSquareModelListener.class))).thenReturn(mockModel);
        when(mockFactory.fourSquareModelFactoryInstance()).thenReturn(mockModelFactory);
        when(mockModel.isReady()).thenReturn(true);
        try {
            when(mockJsonObject.getJSONObject(anyString())).thenReturn(mockJsonObject);
            when(mockJsonObject.getJSONArray(anyString())).thenReturn(mockJsonArray);
            when(mockJsonArray.length()).thenReturn(1);
            when(mockJsonArray.getJSONObject(0)).thenReturn(mockJsonHit1Object);
            when(mockJsonHit1Object.getJSONObject(anyString())).thenReturn(mockJsonHit1Object);
            when(mockJsonHit1Object.getString("name")).thenReturn("name 1");
            when(mockJsonHit1Object.getString("address")).thenReturn("road 1");
            when(mockJsonHit1Object.getString("distance")).thenReturn("5551");
        } catch(Exception e) {
        }

        FourSquarePresenter presenter = new FourSquarePresenter(mockContext, mockFactory);
        presenter.setView(mockView);
        assertEquals(presenter.isReady(), true);

        presenter.onSuccess(mockJsonObject);
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(mockView, times(1)).updateSearchResults(argument.capture());
        assertEquals(1, argument.getValue().size());
        HashMap<String,String> hit = (HashMap<String,String>) argument.getValue().get(0);
        assertEquals("name 1", hit.get("name"));
        assertEquals("road 1", hit.get("address"));
        assertEquals("5551", hit.get("distance"));
    }
}
