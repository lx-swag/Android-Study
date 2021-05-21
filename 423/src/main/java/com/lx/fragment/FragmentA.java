package com.lx.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

public class FragmentA extends ListFragment {
    private String[] cities = {"北京","上海","广州","深圳"};
    private SelectedCityListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setListAdapter(new ArrayAdapter<String>(context,
                                                android.R.layout.simple_list_item_1,
                                                android.R.id.text1,
                                                cities));
    }
    public void setOnSelectedCityListener(SelectedCityListener listener){
        this.listener = listener;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        listener.selectCity(cities[position]);
        super.onListItemClick(l, v, position, id);
    }
}
