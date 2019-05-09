package com.example.asus.ele_me.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asus.ele_me.CustomViewPager;
import com.example.asus.ele_me.NavigationItemBean;
import com.example.asus.ele_me.R;
import com.example.asus.ele_me.adapter.NavigationAdapter;
import com.example.asus.ele_me.fragment.HomeFragment;
import com.example.asus.ele_me.fragment.MenuFragment;
import com.example.asus.ele_me.util.InjectView;
import com.example.asus.ele_me.util.Injector;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public  class HomePageActivity extends AppCompatActivity {
    @InjectView(R.id.slidingpanellayout)
    private SlidingPaneLayout slidingPaneLayout;
    private MenuFragment menuFragment;
    private HomeFragment contentFragment;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int maxMargin = 0;


   //@Bind(R.id.vp_navigation)
   // CustomViewPager vpNavigation;
   // @Bind(R.id.ll_dots)
    LinearLayout llDots;
    private NavigationAdapter adapter;
    private List<NavigationItemBean> itemBeanList = new ArrayList<>();
    private List<ImageView> dots = new ArrayList<>();

    //图标id数组
    private int[] icons = {
            R.mipmap.ic_category_0,
            R.mipmap.ic_category_1,
            R.mipmap.ic_category_2,
            R.mipmap.ic_category_3,
            R.mipmap.ic_category_4,
            R.mipmap.ic_category_5,
            R.mipmap.ic_category_6,
            R.mipmap.ic_category_7,
            R.mipmap.ic_category_8,
            R.mipmap.ic_category_10,
            R.mipmap.ic_category_11,
            R.mipmap.ic_category_12,
            R.mipmap.ic_category_13,
            R.mipmap.ic_category_14,
            R.mipmap.ic_category_16,
            R.mipmap.ic_category_15};

    //图标名称数组
    private String[] names = {"美食",
            "电影",
            "酒店",
            "KTV",
            "生活服务",
            "旅游",
            "学习培训",
            "汽车服务",
            "摄影培训",
            "休闲娱乐",
            "丽人",
            "运动健身",
            "大保健",
            "团购",
            "景点",
            "全部分类"};


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setContentView(R.layout.slidingpane_main_layout);

       // ButterKnife.bind(this);
        initData();

        Injector.get(this).inject();//init views
        menuFragment = new MenuFragment();
        contentFragment = new HomeFragment();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.slidingpane_menu, menuFragment);
        transaction.replace(R.id.slidingpane_content, contentFragment);
        transaction.commit();
        maxMargin = displayMetrics.heightPixels / 10;
        slidingPaneLayout.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // TODO Auto-generated method stub
//
            }

            @Override
            public void onPanelOpened(View panel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPanelClosed(View panel) {
                // TODO Auto-generated method stub

            }
        });

    }



    private void initData() {
        for (int i = 0; i < icons.length; i++) {
            itemBeanList.add(new NavigationItemBean(icons[i], names[i]));
        }
        //adapter = new NavigationAdapter(this, itemBeanList, this);

    }

    /**
     * @return the slidingPaneLayout
     */
    public SlidingPaneLayout getSlidingPaneLayout() {
        return slidingPaneLayout;
    }

}

