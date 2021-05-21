package com.lx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GridViewActivity extends AppCompatActivity {
    private  String[] address = {"北京","上海","广州","深圳","苏州","成都","香港"};
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        mGridView = findViewById(R.id.gridView);
        mGridView.setAdapter(new MyAdapter());
    }
     class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return address.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null){
                 view = LayoutInflater.from(GridViewActivity.this).inflate(R.layout.grid_item, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = view.findViewById(R.id.textView2);
                view.setTag(viewHolder);
            }else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
//            TextView textView = viewHolder.textView;
//            textView.setText();
            viewHolder.textView.setText(address[position]);
            return view;
        }
    }
    class ViewHolder{
        TextView textView;
    }
}