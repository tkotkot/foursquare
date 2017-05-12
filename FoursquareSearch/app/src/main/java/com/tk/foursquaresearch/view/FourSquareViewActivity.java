package com.tk.foursquaresearch.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.tk.foursquaresearch.R;
import com.tk.foursquaresearch.presenter.FourSquarePresenterFactory;
import com.tk.foursquaresearch.view.util.CustomItemData;
import com.tk.foursquaresearch.view.util.CustomListAdapter;
import com.tk.foursquaresearch.view.util.CustomListItemHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FourSquareViewActivity extends AppCompatActivity implements FourSquareViewInterface {
    private CustomListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_square_view);

        ListView listView = (ListView) findViewById(R.id.searchList);
        EditText editText = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.clearButton);

        List<CustomItemData> items = new ArrayList<CustomItemData>();
        adapter = new CustomListAdapter(this, R.layout.custom_list_item, items);
        listView.setAdapter(adapter);

        final Context context = this;
        editText.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                FourSquarePresenterFactory.getPresenter(context).search(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        button.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        FourSquarePresenterFactory.getPresenter(this).initialize();
        FourSquarePresenterFactory.getPresenter(this).setView(this);
        if(FourSquarePresenterFactory.getPresenter(this).isReady()) {
            setUIReady();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        FourSquarePresenterFactory.getPresenter(this).setView(null);
    }

    @Override
    public void searchReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setUIReady();
            }
        });
    }

    @Override
    public void updateSearchResults(final List<HashMap<String,String>> result) {
        final List<CustomItemData> data = CustomListItemHelper.getList(result);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(data);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void displayLocationError() {
    }

    @Override
    public void displayNetworkError() {

    }

    public void onClick(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText("");
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    private void setUIReady() {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setEnabled(true);
        editText.setHint(R.string.hint_text_ready);

        Button button = (Button) findViewById(R.id.clearButton);
        button.setEnabled(true);
    }
}
