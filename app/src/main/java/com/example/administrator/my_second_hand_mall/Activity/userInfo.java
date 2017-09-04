package com.example.administrator.my_second_hand_mall.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.example.administrator.my_second_hand_mall.DAO.Utils;
import com.example.administrator.my_second_hand_mall.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class userInfo extends AppCompatActivity {

    private CircleImageView user_icon;
    private TextView user_name;
    private EditText edit_phone;
    private EditText edit_oldpwd;
    private EditText edit_newpwd;
    private TextView txt_bianji;
    private TextView txt_cancel;
    private Button btn_signout;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        user_icon = (CircleImageView) findViewById(R.id.user_icon);
        user_name = (TextView) findViewById(R.id.user_name);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_oldpwd = (EditText) findViewById(R.id.edit_oldpwd);
        edit_newpwd = (EditText) findViewById(R.id.edit_newpwd);
        txt_bianji = (TextView) findViewById(R.id.txt_bianji);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        btn_signout = (Button) findViewById(R.id.btn_signOut);

        txt_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_bianji.getText().toString().equals("编辑")){
                    txt_bianji.setText("提交");
                    edit_phone.setEnabled(true);
                    edit_oldpwd.setEnabled(true);
                    edit_newpwd.setEnabled(true);
                    txt_cancel.setVisibility(View.VISIBLE);
                }
                else {//将修改数据提交
                    if(!Utils.isMobileNO(edit_phone.getText().toString())){
                        Toast.makeText(getBaseContext(),"手机号码格式不正确",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(!edit_oldpwd.getText().toString().equals("") || !edit_newpwd.getText().toString().equals("")){
                        if(!edit_oldpwd.getText().toString().equals(Utils.currentUser.getPassword())){
                            Toast.makeText(getBaseContext(),"旧密码错误",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(edit_newpwd.getText().toString().equals("")){
                            Toast.makeText(getBaseContext(),"新密码不能为空",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(edit_newpwd.getText().toString().length() < 6){
                            Toast.makeText(getBaseContext(),"密码长度最小为6位",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            //提交带有密码修改
                            Utils.currentUser.setMobilePhoneNumber(edit_phone.getText().toString());
                            Utils.currentUser.setPassword(edit_newpwd.getText().toString());
                            Utils.updateUserInfo(Utils.currentUser, handler);
                        }
                    }
                    //提交不带密码修改
                    Utils.currentUser.setMobilePhoneNumber(edit_phone.getText().toString());
                    Utils.updateUserInfo(Utils.currentUser, handler);
                }
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_bianji.setText("编辑");
                edit_phone.setEnabled(false);
                edit_oldpwd.setEnabled(false);
                edit_newpwd.setEnabled(false);
                edit_phone.setText(Utils.currentUser.getMobilePhoneNumber());
                edit_oldpwd.setText("");
                edit_newpwd.setText("");
                txt_cancel.setVisibility(View.GONE);
            }
        });

        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.signIn = false;
                onBackPressed();
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(getBaseContext(),"修改成功",Toast.LENGTH_SHORT).show();
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
        if(Utils.fileIsExists(Utils.iconPath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(Utils.iconPath);
            user_icon.setImageBitmap(bitmap);
        }
        else {
            Glide.with(getBaseContext()).load(Utils.currentUser.getUser_icon().getUrl()).into(user_icon);
        }
        user_name.setText(Utils.currentUser.getUsername());
        edit_phone.setText(Utils.currentUser.getMobilePhoneNumber());
    }
}
