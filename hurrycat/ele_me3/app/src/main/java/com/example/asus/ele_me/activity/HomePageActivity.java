package com.example.asus.ele_me.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.example.asus.ele_me.R;
import com.example.asus.ele_me.fragment.HomeFragment;
import com.example.asus.ele_me.fragment.MenuFragment;
import com.example.asus.ele_me.util.InjectView;
import com.example.asus.ele_me.util.Injector;

public class HomePageActivity extends Activity {
    @InjectView(R.id.slidingpanellayout)
    private SlidingPaneLayout slidingPaneLayout;
    private MenuFragment menuFragment;
    private HomeFragment contentFragment;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int maxMargin = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setContentView(R.layout.slidingpane_main_layout);
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

    /**
     * @return the slidingPaneLayout
     */
    public SlidingPaneLayout getSlidingPaneLayout() {
        return slidingPaneLayout;
    }

}

