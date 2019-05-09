package com.example.asus.ele_me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class Pay extends AppCompatActivity {

    private TextView tr_tv_good_price;
    private ImageView tr_back_bt;
    private TextView tr_tv_good_name;
    private String restaurant_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);
        tr_back_bt=findViewById(R.id.tr_back_bt);
        tr_tv_good_name=findViewById(R.id.tr_tv_good_name);
        tr_tv_good_price=findViewById(R.id.tr_tv_good_price);
        Intent i=getIntent();
        String value=i.getStringExtra("price");
        tr_tv_good_price.setText(value);
        tr_back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.tr_back_bt){
                    finish();
                }
            }
        });
    }
}
