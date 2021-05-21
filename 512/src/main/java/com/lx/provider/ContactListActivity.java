package com.lx.provider;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;
@SuppressLint("NewApi")
public class ContactListActivity extends AppCompatActivity {
    private ListView listView;
    private ContactAdapter adapter;
    private List<Map<String,String>> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1);
        setContentView(R.layout.activity_contact_list);
        listView = findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String number = data.get(position).get("number");
                Intent intent = getIntent();
                intent.putExtra("number",number);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        adapter = new ContactAdapter();
        //查询得到联系人的数据
        ContentResolver resolver = getContentResolver();
        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(0);
            String number = cursor.getString(1);
            Map<String,String> map = new HashMap<>();
            map.put("name",name);
            map.put("number",number);
            data.add(map);
        }
        listView.setAdapter(adapter);
    }
    class ContactAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView == null){
                view = LayoutInflater.from(ContactListActivity.this).inflate(R.layout.list_item,null);
                holder = new ViewHolder();
                holder.name = view.findViewById(R.id.item_name);
                holder.number = view.findViewById(R.id.item_number);
                view.setTag(holder);
                convertView = view;
            }else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            Map<String,String> map = data.get(position);
            holder.name.setText(map.get("name"));
            holder.number.setText(map.get("number"));
            return view;
        }
    }
    class ViewHolder{
         TextView name;
         TextView number;

//        public TextView getName() {
//            return name;
//        }
//
//        public TextView getNumber() {
//            return number;
//        }
    }
}