package com.example.administrator.my_second_hand_mall.Activity;

import android.content.Intent;
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

public class FindpwdActivity extends AppCompatActivity {

    private TextView txt_problem;
    private EditText edit_answer;
    private TextView txt_pwd;
    private Button btn_findpwd;
    private String problem;
    private String answer;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        txt_problem = (TextView) findViewById(R.id.txt_problem);
        txt_pwd = (TextView) findViewById(R.id.txt_pwd);
        edit_answer = (EditText) findViewById(R.id.edit_answer);
        btn_findpwd = (Button) findViewById(R.id.btn_findpwd);

        Intent intent = getIntent();
        problem = intent.getStringExtra("problem");
        answer = intent.getStringExtra("answer");
        pwd = intent.getStringExtra("pwd");

        btn_findpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_answer.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"请输入问题的答案",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    if(edit_answer.getText().toString().equals(answer)){
                        txt_pwd.setText(pwd);
                    }
                    else {
                        Toast.makeText(getBaseContext(),"答案错误",Toast.LENGTH_SHORT).show();
                    }
                }
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
        txt_problem.setText(problem);
    }
}
