package com.tk.foursquaresearch.view.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tk.foursquaresearch.R;
import com.tk.foursquaresearch.view.util.CustomItemData;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<CustomItemData> {

    private int layoutResource;

    public CustomListAdapter(Context context, int layoutResource, List<CustomItemData> data) {
        super(context, layoutResource, data);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        CustomItemData item = getItem(position);

        if (item != null) {
            TextView topTextView = (TextView) view.findViewById(R.id.textViewTop);
            TextView middleTextView = (TextView) view.findViewById(R.id.textViewMiddle);
            TextView bottomTextView = (TextView) view.findViewById(R.id.textViewBottom);

            if (topTextView != null) {
                topTextView.setText(item.getTop());
            }

            if (middleTextView != null) {
                middleTextView.setText(item.getMiddle());
            }

            if (bottomTextView != null) {
                bottomTextView.setText(item.getBottom());
            }
        }

        return view;
    }
}