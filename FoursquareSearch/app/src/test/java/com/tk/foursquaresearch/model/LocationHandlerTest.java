package com.tk.foursquaresearch.model;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.tk.foursquaresearch.model.FourSquareModelFactoryInterface;
import com.tk.foursquaresearch.model.util.LocationHandler;
import com.tk.foursquaresearch.model.util.LocationHandlerInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static android.content.Context.LOCATION_SERVICE;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocationHandlerTest {
    FourSquareModelFactoryInterface mockFactory = null;
    LocationManager mockLocationManager = null;
    Context mockContext = null;
    LocationHandlerInterface mockListener = null;
    Location mockLocation = null;

    @Before
    public void initializeTest() {
        mockLocationManager = mock(LocationManager.class);
        mockContext = mock(Context.class);
        mockListener = mock(LocationHandlerInterface.class);
        mockLocation = mock(Location.class);
        mockFactory = mock(FourSquareModelFactoryInterface.class);
    }

    @Test
    public void testConstruct()  {
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
            verify(mockLocationManager, times(1)).requestLocationUpdates(matches(LocationManager.GPS_PROVIDER), anyLong(), anyFloat(), any(LocationListener.class));
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

    @Test
    public void testInitializeNetwork()  {
        when(mockContext.getSystemService(LOCATION_SERVICE)).thenReturn(mockLocationManager);
        when(mockLocation.getLatitude()).thenReturn(0d);
        when(mockLocation.getLongitude()).thenReturn(0d);
        when(mockFactory.locationInstance()).thenReturn(mockLocation);
        when(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false);
        when(mockLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(true);

        try {
            when(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(mockLocation);
        } catch(SecurityException e) {
        }

        LocationHandler locationHandler = new LocationHandler(mockContext, mockFactory, mockListener);
        locationHandler.initialize();

        try {
            verify(mockLocationManager, times(1)).requestLocationUpdates(matches(LocationManager.NETWORK_PROVIDER), anyLong(), anyFloat(), any(LocationListener.class));
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

    @Test
    public void testInitializeError()  {
        when(mockContext.getSystemService(LOCATION_SERVICE)).thenReturn(mockLocationManager);
        when(mockLocation.getLatitude()).thenReturn(0d);
        when(mockLocation.getLongitude()).thenReturn(0d);
        when(mockFactory.locationInstance()).thenReturn(mockLocation);
        when(mockLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false);
        when(mockLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false);

        LocationHandler locationHandler = new LocationHandler(mockContext, mockFactory, mockListener);
        locationHandler.initialize();

        try {
            verify(mockLocationManager, never()).requestLocationUpdates(matches(LocationManager.NETWORK_PROVIDER), anyLong(), anyFloat(), any(LocationListener.class));
        } catch(SecurityException e) {
        }
        verify(mockListener, times(1)).onLocationError();
        verify(mockListener, never()).onLocationUpdate();
        assertEquals(locationHandler.isActive(), false);
    }
}
