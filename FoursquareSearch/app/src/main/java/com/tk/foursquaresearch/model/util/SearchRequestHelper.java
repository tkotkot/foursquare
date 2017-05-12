package com.tk.foursquaresearch.model.util;

import java.net.URL;

public class SearchRequestHelper {
    public static URL generateRequestUrl(String id, String pwd, double longitude, double latitude, String criteria) {
        URL url = null;
        try {
            String urlPattern = "https://api.foursquare.com/v2/venues/search?client_id=%s&client_secret=%s&v=20170501&limit=10&ll=%f,%f&query=%s";
            url = new URL(String.format(urlPattern, id, pwd, longitude, latitude, criteria));
        } catch(Exception e) {
        }
        return url;
    }
}
