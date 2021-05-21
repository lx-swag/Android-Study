package com.lx.fragment;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class MyFragment extends Fragment {
    private TextView textView;
    private int count;
    public static MyFragment newInstance(int num){
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count",num);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        int num = bundle.getInt("count");
        count = num;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textView = view.findViewById(R.id.textView);
        textView.setText("fragment: "+count);
        super.onViewCreated(view, savedInstanceState);
    }
    public void setText(String text){
        textView.setText(text);
    }
}
