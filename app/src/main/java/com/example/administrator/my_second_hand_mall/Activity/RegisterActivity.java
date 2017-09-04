package com.example.administrator.my_second_hand_mall.Activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.my_second_hand_mall.DAO.PathGetter;
import com.example.administrator.my_second_hand_mall.DAO.Utils;
import com.example.administrator.my_second_hand_mall.DAO.initBmob;
import com.example.administrator.my_second_hand_mall.R;
import com.example.administrator.my_second_hand_mall.bmobObject.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;



public class RegisterActivity extends AppCompatActivity {

    private boolean ischoosepic;
    private CircleImageView img_addimg;
    private EditText edit_id;
    private EditText edit_pwd1;
    private EditText edit_pwd2;
    private EditText edit_phone;
    private Button btn_register;
    private Button btn_reset;
    private Spinner sp_problem;
    private EditText edit_answer;
    private Uri uri;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ischoosepic = false;
        img_addimg = (CircleImageView) findViewById(R.id.img_addimg);
        edit_id = (EditText) findViewById(R.id.edit_name);
        edit_pwd1 = (EditText) findViewById(R.id.edit_price);
        edit_pwd2 = (EditText) findViewById(R.id.edit_describe);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        sp_problem = (Spinner) findViewById(R.id.sp_problem);
        edit_answer = (EditText) findViewById(R.id.edit_answer);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        initBmob.initMyBmob(this);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_addimg.setImageResource(R.drawable.add_pro);
                uri = null;
                ischoosepic = false;
                edit_id.setText(null);
                edit_pwd1.setText(null);
                edit_pwd2.setText(null);
                edit_phone.setText(null);
                edit_answer.setText(null);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ischoosepic){
                    Toast.makeText(getBaseContext(),"请选择头像",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edit_id.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"请输入用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edit_id.getText().toString().length() < 11){
                    Toast.makeText(getBaseContext(),"用户名长度不能小于11位",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edit_pwd1.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edit_pwd1.getText().toString().length() < 6){
                    Toast.makeText(getBaseContext(),"密码长度最小为6位",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Utils.isPwdEqual(edit_pwd1, edit_pwd2)){
                    Toast.makeText(getBaseContext(),"两次密码输入不相同",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Utils.isMobileNO(edit_phone.getText().toString())){
                    Toast.makeText(getBaseContext(),"手机号码格式不正确",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edit_answer.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"选择问题并填写答案",Toast.LENGTH_SHORT).show();
                    return;
                }
                Utils.isIdExist(edit_id.getText().toString(),handler);
            }
        });

        img_addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        user u = (user) msg.obj;
                        if(u.getUsername().equals(edit_id.getText().toString())){
                            Toast.makeText(getBaseContext(),"用户名已注册",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    case 1:
                        File file = new File(PathGetter.getPath(getBaseContext(), uri));
                        final BmobFile bmobFile = new BmobFile(file);
                        bmobFile.upload(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    user u = new user();
                                    u.setUsername(edit_id.getText().toString());
                                    u.setPassword(edit_pwd1.getText().toString());
                                    u.setMobilePhoneNumber(edit_phone.getText().toString());
                                    u.setQuestion(sp_problem.getSelectedItem().toString());
                                    u.setAnswer(edit_answer.getText().toString());
                                    u.setUser_icon(bmobFile);
                                    Utils.setCurrentUser(u);
                                    u.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            Toast.makeText(getBaseContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                            Utils.cacheUser(getBaseContext(), Utils.currentUser);
                                            onBackPressed();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        return false;
                }
                return false;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                FileInputStream fileInputStream = new FileInputStream(new File(PathGetter.getPath(this, uri)));
                if(fileInputStream.available() < 1048567){
                    img_addimg.setImageBitmap(bitmap);
                    ischoosepic = true;
                }
                else {
                    Toast.makeText(getBaseContext(),"图片不能大于1M，请重新选择",Toast.LENGTH_SHORT).show();
                    img_addimg.setImageResource(R.drawable.add_pro);
                    uri = null;
                    ischoosepic = false;
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(getBaseContext(),"图片返回出错",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
