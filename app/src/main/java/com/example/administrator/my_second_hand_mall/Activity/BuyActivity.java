package com.example.administrator.my_second_hand_mall.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.my_second_hand_mall.DAO.Utils;
import com.example.administrator.my_second_hand_mall.R;
import com.example.administrator.my_second_hand_mall.bmobObject.qg;

import java.io.FileNotFoundException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class BuyActivity extends AppCompatActivity {

    private EditText edit_name;
    private EditText edit_price;
    private EditText edit_describe;
    private Spinner sp_ca;
    private Spinner sp_classify;
    private Button btn_fabu;
    private Button btn_reset;
    private Handler handler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_price = (EditText) findViewById(R.id.edit_price);
        edit_describe = (EditText) findViewById(R.id.edit_describe);
        sp_ca = (Spinner) findViewById(R.id.sp_ca);
        sp_classify = (Spinner) findViewById(R.id.sp_classify);
        btn_fabu = (Button) findViewById(R.id.btn_fabu);
        btn_reset = (Button) findViewById(R.id.btn_reset);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_name.setText(null);
                edit_price.setText(null);
                edit_describe.setText(null);
            }
        });

        btn_fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_name.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"请输入求购商品名",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edit_price.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"请输入期望价格",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edit_describe.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"请输入商品描述",Toast.LENGTH_SHORT).show();
                    return;
                }
                Utils.isqgFabu(handler);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        qg q = new qg();
                        q.setUser_iconUrl(Utils.currentUser.getUser_icon().getUrl());
                        q.setUsername(Utils.currentUser.getUsername());
                        q.setQgsp_name(edit_name.getText().toString());
                        q.setPrice(edit_price.getText().toString());
                        q.setDescribe(edit_describe.getText().toString());
                        q.setCategory(sp_ca.getSelectedItem().toString());
                        q.setClassify(sp_classify.getSelectedItem().toString());
                        q.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                Toast.makeText(getBaseContext(), "发布成功", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        });
                        return false;

                    case 1:
                        Toast.makeText(getBaseContext(),"发布数量已达上限",Toast.LENGTH_SHORT).show();
                        return false;
                }
                return false;
            }
        });

        sp_ca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (sp_ca.getSelectedItem().toString()){
                    case "电器":
                        sp_classify.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Utils.getStringArray(getBaseContext(), R.array.classify1)));
                        break;
                    case "书籍":
                        sp_classify.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Utils.getStringArray(getBaseContext(), R.array.classify2)));
                        break;
                    case "运动":
                        sp_classify.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Utils.getStringArray(getBaseContext(), R.array.classify3)));
                        break;
                    case "生活":
                        sp_classify.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Utils.getStringArray(getBaseContext(), R.array.classify4)));
                        break;
                    case "其他":
                        sp_classify.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, Utils.getStringArray(getBaseContext(), R.array.classify5)));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
}
