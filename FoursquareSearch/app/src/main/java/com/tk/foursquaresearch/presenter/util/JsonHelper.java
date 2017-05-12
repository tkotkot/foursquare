package com.tk.foursquaresearch.presenter.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonHelper {
    public static JSONObject getObject(JSONObject jsonObject, String item) {
        JSONObject object = new JSONObject();
        try {
            object = jsonObject.getJSONObject(item);
        } catch(Exception e) {
        }
        return object;
    }

    public static JSONArray getArray(JSONObject jsonObject, String item) {
        JSONArray array = new JSONArray();
        try {
            array = jsonObject.getJSONArray(item);
        } catch(Exception e) {
        }
        return array;
    }

    public static JSONObject getObjectByIndex(JSONArray jsonArray, int index) {
        JSONObject object = new JSONObject();
        try {
            object = jsonArray.getJSONObject(index);
        } catch(Exception e) {
        }
        return object;
    }

    public static String getString(JSONObject jsonObject, String item) {
        String value = "";
        try {
            value = jsonObject.getString(item);
        } catch(Exception e) {
        }
        return value;
    }

}
