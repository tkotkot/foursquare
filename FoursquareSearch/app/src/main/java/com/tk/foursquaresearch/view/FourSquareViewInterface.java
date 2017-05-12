package com.tk.foursquaresearch.view;

import java.util.HashMap;
import java.util.List;

public interface FourSquareViewInterface {
    public void searchReady();
    public void updateSearchResults(List<HashMap<String,String>> result);
    public void displayLocationError();
    public void displayNetworkError();
}
