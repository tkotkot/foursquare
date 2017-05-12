package com.tk.foursquaresearch.model.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.tk.foursquaresearch.model.FourSquareModelFactoryInterface;

import static android.content.Context.LOCATION_SERVICE;

public class LocationHandler implements LocationListener {
    private Context context = null;
    private FourSquareModelFactoryInterface modelFactory = null;
    private LocationManager locationManager = null;
    private LocationHandlerInterface listener = null;
    private Location location = null;
    private boolean active = false;

    public LocationHandler(Context ctx, FourSquareModelFactoryInterface factory, LocationHandlerInterface handlerListener) {
        context = ctx;
        modelFactory = factory;
        listener = handlerListener;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        location = modelFactory.locationInstance();
    }

    public void initialize() {
        requestUpdate();
    }

    public boolean isActive() {
        return active;
    }

    public boolean isLocationValid() {
        return ((location.getLatitude() != 0) || (location.getLongitude() != 0));
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    private void requestUpdate() {
        if(listener != null) {
           if(requestGpsUpdate()) {
               if(isLocationValid()) {
                   listener.onLocationUpdate();
               }
           } else if(requestNetworkUpdate()) {
               if(isLocationValid()) {
                   listener.onLocationUpdate();
               }
           } else {
               listener.onLocationError();
           }
        }
    }

    private boolean requestGpsUpdate() {
        return requestProviderUpdate(LocationManager.GPS_PROVIDER);
    }

    private boolean requestNetworkUpdate() {
        return requestProviderUpdate(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestProviderUpdate(String provider) {
        boolean isEnabled = locationManager.isProviderEnabled(provider);
        if(isEnabled) {
            try {
                locationManager.requestLocationUpdates(
                        provider,
                        10000,
                        50, this);

                Location lastLocation = locationManager.getLastKnownLocation(provider);
                setLocation(lastLocation);
                active = true;
            } catch (SecurityException e) {
            }
        }
        return isEnabled;
    }



    public void onLocationChanged(Location newLocation) {
        setLocation(newLocation);
        if (listener != null) {
            listener.onLocationUpdate();
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    private void setLocation(Location newLocation) {
        if(newLocation != null) {
            location = newLocation;
        }
    }
}
