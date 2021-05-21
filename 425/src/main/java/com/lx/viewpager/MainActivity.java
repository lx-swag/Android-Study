package com.lx.viewpager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends FragmentActivity {
//    private View[] views;
    private Fragment[] fragments;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TabLayoutMediator mediator;

    private String[] titles = {"fragment1","fragment2","fragment3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        viewPager.setAdapter(new MyAdapter(this));
        //禁用预加载
        viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        //页面切换监听
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                int count = tabLayout.getTabCount();
//                for (int i = 0; i < count; i++) {
//                    TabLayout.Tab tab = tabLayout.getTabAt(i);
//                    TextView tabView = (TextView) tab.getCustomView();
//                    if (tab.getPosition() == position) {
//                        tabView.setTextSize(activeSize);
//                        tabView.setTypeface(Typeface.DEFAULT_BOLD);
//                    } else {
//                        tabView.setTextSize(normalSize);
//                        tabView.setTypeface(Typeface.DEFAULT);
//                    }
//                }
//            }
//        });
        mediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //这里可以自定义TabView
//                TextView tabView = new TextView(MainActivity.this);
//
//                int[][] states = new int[2][];
//                states[0] = new int[]{android.R.attr.state_selected};
//                states[1] = new int[]{};
//
//                int[] colors = new int[]{activeColor, normalColor};
//                ColorStateList colorStateList = new ColorStateList(states, colors);
//                tabView.setText(titles[position]);
//                tabView.setTextSize(normalSize);
//                tabView.setTextColor(colorStateList);
//
//                tab.setCustomView(tabView);
                tab.setText(titles[position]);
            }
        });
        mediator.attach();

    }
    public void init(){
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layout);
//        views = new View[3];
//        LayoutInflater inflater = LayoutInflater.from(this);
//        views[0] = inflater.inflate(R.layout.view1,null);
//        views[1] = inflater.inflate(R.layout.view2,null);
//        views[2] = inflater.inflate(R.layout.view3,null);
        fragments = new Fragment[3];
        fragments[0] = new MyFragment1();
        fragments[1] = new MyFragment2();
        fragments[2] = new MyFragment3();
    }
    class MyAdapter extends FragmentStateAdapter{


        public MyAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments[position];
        }

        @Override
        public int getItemCount() {
            return fragments.length;
        }
    }

//    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{
//
//        @NonNull
//        @Override
//        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent,false);
//            return new MyHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
//            holder.layout.addView(views[position]);
//        }
//        @Override
//        public int getItemCount() {
//            return views.length;
//        }
//        class MyHolder extends RecyclerView.ViewHolder{
//            RelativeLayout layout;
//            public MyHolder(@NonNull View itemView) {
//                super(itemView);
//                layout = itemView.findViewById(R.id.viewPagerLayout);
//            }
//        }
//    }
}