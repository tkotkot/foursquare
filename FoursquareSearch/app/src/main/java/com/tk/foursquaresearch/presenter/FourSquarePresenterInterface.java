package com.tk.foursquaresearch.presenter;

import com.tk.foursquaresearch.view.FourSquareViewInterface;

public interface FourSquarePresenterInterface {
    void initialize();
    boolean isReady();
    void setView(FourSquareViewInterface view);
    boolean search(String criteria);
}
