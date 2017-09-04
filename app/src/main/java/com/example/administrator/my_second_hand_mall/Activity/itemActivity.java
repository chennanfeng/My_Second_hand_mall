package com.example.administrator.my_second_hand_mall.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.my_second_hand_mall.DAO.Utils;
import com.example.administrator.my_second_hand_mall.R;
import com.example.administrator.my_second_hand_mall.bmobObject.user;

public class itemActivity extends AppCompatActivity {

    private String Describe;
    private String Name;
    private String Price;
    private String Url;
    private String username;
    private ImageView sp_img;
    private TextView txt_price;
    private TextView txt_describe;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton btn_phone;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        sp_img = (ImageView) findViewById(R.id.sp_img);
        txt_price = (TextView) findViewById(R.id.txt_price);
        txt_describe = (TextView) findViewById(R.id.txt_describe);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        btn_phone = (FloatingActionButton) findViewById(R.id.btn_phone);

        final Intent intent = getIntent();
        Describe = intent.getStringExtra("Describe");
        Name = intent.getStringExtra("Name");
        Price = intent.getStringExtra("Price");
        Url = intent.getStringExtra("Url");
        username = intent.getStringExtra("username");

        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.isIdExist(username, handler);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        user u = (user) msg.obj;
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        i.setData(Uri.parse("tel:" + u.getMobilePhoneNumber()));
                        startActivity(i);
                        return false;
                }
                return false;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        collapsingToolbar.setTitle(Name);
        txt_describe.setText(Describe);
        txt_price.setText(Price + "￥");
        Glide.with(getBaseContext()).load(Url).placeholder(R.drawable.header_background)
                .error(R.drawable.header_background).into(sp_img);
    }
}
