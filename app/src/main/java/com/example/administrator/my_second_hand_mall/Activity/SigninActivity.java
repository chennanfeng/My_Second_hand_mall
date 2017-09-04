package com.example.administrator.my_second_hand_mall.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_second_hand_mall.DAO.Utils;
import com.example.administrator.my_second_hand_mall.R;
import com.example.administrator.my_second_hand_mall.bmobObject.user;

public class SigninActivity extends AppCompatActivity {

    private Button btn_signin;
    private Button btn_toregister;
    private TextView txt_findpwd;
    private EditText edit_name;
    private EditText edit_pwd;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_toregister = (Button) findViewById(R.id.btn_toregister);
        txt_findpwd = (TextView) findViewById(R.id.txt_findpwd);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_pwd = (EditText) findViewById(R.id.edit_describe);

        txt_findpwd.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        txt_findpwd.getPaint().setAntiAlias(true);//抗锯齿

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }


        //找回密码点击事件
        txt_findpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_name.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(),"请输入帐号后再找回密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Utils.isIdExist(edit_name.getText().toString(), handler);
                }
            }
        });

        //登录按钮事件
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_name.getText().toString().equals("") && !edit_pwd.getText().toString().equals("")) {
                    Utils.iconPath = "";
                    Utils.isSignIn(edit_name.getText().toString(), edit_pwd.getText().toString(), handler);
                }
                else {
                    Toast.makeText(getBaseContext(),"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //注册按钮事件
        btn_toregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        user u = (user) msg.obj;
                        Intent intent = new Intent(SigninActivity.this, FindpwdActivity.class);
                        intent.putExtra("problem",u.getQuestion());
                        intent.putExtra("answer",u.getAnswer());
                        intent.putExtra("pwd",u.getPassword());
                        startActivity(intent);
                        return false;

                    case 1:
                        Toast.makeText(getBaseContext(),"帐号不存在",Toast.LENGTH_SHORT).show();
                        return false;

                    case 2:
                        Toast.makeText(getBaseContext(),"登录失败",Toast.LENGTH_SHORT).show();
                        return false;

                    case 3:
                        Toast.makeText(getBaseContext(),"登录成功",Toast.LENGTH_SHORT).show();
                        Utils.cacheUser(getBaseContext(), Utils.currentUser);
                        Utils.signIn = true;
                        onBackPressed();
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
        if(!Utils.currentUser.getUsername().equals("")){
            edit_name.setText(Utils.currentUser.getUsername());
        }
    }

}
