package com.tk.foursquaresearch.model;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.tk.foursquaresearch.model.FourSquareModelFactoryInterface;
import com.tk.foursquaresearch.model.util.LocationHandler;
import com.tk.foursquaresearch.model.util.LocationHandlerInterface;

import org.junit.Test;
import org.mockito.Mockito;
import static android.content.Context.LOCATION_SERVICE;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocationHandlerTest {

    @Test
    public void testConstruct()  {
        LocationManager mockLocationManager = mock(LocationManager.class);
        Context mockContext = mock(Context.class);
        LocationHandlerInterface mockListener = mock(LocationHandlerInterface.class);
        Location mockLocation = mock(Location.class);
        FourSquareModelFactoryInterface mockFactory = mock(FourSquareModelFactoryInterface.class);
        when(mockContext.getSystemService(LOCATION_SERVICE)).thenReturn(mockLocationManager);
        when(mockLocation.getLatitude()).thenReturn(0d);
        when(mockLocation.getLongitude()).thenReturn(0d);
        when(mockFactory.locationInstance()).thenReturn(mockLocation);

        LocationHandler locationHandler = new LocationHandler(mockContext, mockFactory, mockListener);

        assertEquals(locationHandler.isActive(), false);
        assertEquals(locationHandler.isLocationValid(), false);
    }

    @Test
    public void testInitializeGPS()  {
        LocationManager mockLocationManager = mock(LocationManager.class);
        Context mockContext = mock(Context.class);
        LocationHandlerInterface mockListener = mock(LocationHandlerInterface.class);
        Location mockLocation = mock(Location.class);
        FourSquareModelFactoryInterface mockFactory = mock(FourSquareModelFactoryInterface.class);
        when(mockContext.getSystemService(LOCATION_SERVICE)).thenReturn(mockLocationManager);
        when(mockLocation.getLatitude()).thenReturn(0d);
        when(mockLocation.getLongitude()).thenReturn(0d);
        when(mockFactory.locationInstance()).thenReturn(mockLocation);
        when(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);
        try {
            when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(mockLocation);
        } catch(SecurityException e) {
        }

        LocationHandler locationHandler = new LocationHandler(mockContext, mockFactory, mockListener);
        locationHandler.initialize();

        try {
            verify(mockLocationManager, times(1)).requestLocationUpdates(anyString(), anyLong(), anyFloat(), any(LocationListener.class));
        } catch(SecurityException e) {
        }
        verify(mockListener, never()).onLocationError();
        verify(mockListener, never()).onLocationUpdate();
        assertEquals(locationHandler.isActive(), true);

        Location mockLocation2 = mock(Location.class);
        when(mockLocation2.getLatitude()).thenReturn(10.2d);
        when(mockLocation2.getLongitude()).thenReturn(-1.8d);
        locationHandler.onLocationChanged(mockLocation2);

        assertEquals(locationHandler.isLocationValid(), true);
        assertEquals(locationHandler.getLatitude(), 10.2d);
        assertEquals(locationHandler.getLongitude(), -1.8d);
        verify(mockListener, never()).onLocationError();
        verify(mockListener, times(1)).onLocationUpdate();
    }

}
