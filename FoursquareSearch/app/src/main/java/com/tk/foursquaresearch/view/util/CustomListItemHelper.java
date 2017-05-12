package com.tk.foursquaresearch.view.util;


import com.tk.foursquaresearch.view.util.CustomItemData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomListItemHelper {
    public static List<CustomItemData> getList(List<HashMap<String,String>> result)  {
        List<CustomItemData> items = new ArrayList<CustomItemData>();
        for(HashMap<String,String> item: result) {
            try {
                items.add(new CustomItemData(item.get("name"), item.get("address"), item.get("distance") + " meters"));
            } catch(Exception e) {
            }
        }
        return items;
    }
}
