package com.example.administrator.my_second_hand_mall.Activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_second_hand_mall.Adapter.spAdapter;
import com.example.administrator.my_second_hand_mall.DAO.Utils;
import com.example.administrator.my_second_hand_mall.R;
import com.example.administrator.my_second_hand_mall.bmobObject.sp;

import java.util.ArrayList;
import java.util.List;

public class MysellActivity extends AppCompatActivity {

    private List<sp> spList = new ArrayList<sp>();
    private Handler handler;
    private TextView txt_bianji;
    private TextView txt_cancel;
    private RecyclerView mysell_recyclerView;
    private spAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysell);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }

        txt_bianji = (TextView) findViewById(R.id.txt_bianji);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        mysell_recyclerView = (RecyclerView) findViewById(R.id.mysell_recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.load_progress);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mysell_recyclerView.setLayoutManager(layoutManager);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        spList = (List<sp>) msg.obj;
                        adapter = new spAdapter(spList);
                        mysell_recyclerView.setAdapter(adapter);
                        return false;
                    case 1:
                        Utils.mySellList(progressBar,handler);
                        Toast.makeText(getBaseContext(),"删除成功",Toast.LENGTH_SHORT).show();
                        Utils.spList_del.clear();
                        Utils.canDel = false;
                        txt_bianji.setText("编辑");
                        txt_cancel.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        return false;
                }
                return false;
            }
        });

        txt_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_bianji.getText().toString().equals("编辑")) {
                    Utils.canDel = true;
                    txt_bianji.setText("删除");
                    txt_cancel.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
                else if(txt_bianji.getText().toString().equals("删除")) {
                    if(Utils.spList_del.size() >  0){
                        Utils.spList_delete(Utils.spList_del, handler);
                    }
                    else {
                        Toast.makeText(getBaseContext(),"请选择需要删除的选项",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.canDel = false;
                txt_bianji.setText("编辑");
                txt_cancel.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
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
        Utils.canDel = false;
        Utils.mySellList(progressBar,handler);
    }
}
