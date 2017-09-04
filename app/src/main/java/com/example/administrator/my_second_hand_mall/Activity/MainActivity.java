package com.example.administrator.my_second_hand_mall.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.my_second_hand_mall.Adapter.qgAdapter;
import com.example.administrator.my_second_hand_mall.Adapter.spAdapter;
import com.example.administrator.my_second_hand_mall.DAO.Utils;
import com.example.administrator.my_second_hand_mall.DAO.initBmob;
import com.example.administrator.my_second_hand_mall.Dialog.Classify_Dialog;
import com.example.administrator.my_second_hand_mall.Dialog.Search_Dialog;
import com.example.administrator.my_second_hand_mall.R;
import com.example.administrator.my_second_hand_mall.View.myRefreshLayout;
import com.example.administrator.my_second_hand_mall.bmobObject.qg;
import com.example.administrator.my_second_hand_mall.bmobObject.sp;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private List<sp> spList = new ArrayList<sp>();
    private List<qg> qgList = new ArrayList<qg>();
    private DrawerLayout mDrawerLayout;
    private View headerView;
    private NavigationView navigationView;
    private CircleImageView icon_image;
    private TextView icon_text;
    private Handler handler;
    private RecyclerView recyclerView;
    private ImageView img_search;
    private LinearLayout ll_sp;
    private LinearLayout ll_qg;
    private spAdapter spAdapter;
    private qgAdapter qgAdapter;
    private myRefreshLayout myRefreshLayout;
    private ProgressBar progressBar;

    private String search_name;
    private String search_minprice;
    private String search_maxprice;
    private String search_category;
    private String search_classify;
    private boolean sellOrBuy;
    private int newSkip;
    private int i;//case5 用到，累计搜索到的数直到大于10


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        search_name = "";
        search_minprice = "";
        search_maxprice = "";
        search_category = "";
        search_classify = "";
        sellOrBuy = true;
        newSkip = 0;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);//设置navigationView的图标显示原来的颜色
        headerView = navigationView.getHeaderView(0);
        icon_image = (CircleImageView) headerView.findViewById(R.id.icon_image);
        icon_text = (TextView) headerView.findViewById(R.id.icon_text);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        img_search = (ImageView) findViewById(R.id.img_search);
        ll_sp = (LinearLayout) findViewById(R.id.ll_sp);
        ll_qg = (LinearLayout) findViewById(R.id.ll_qg);
        myRefreshLayout = (com.example.administrator.my_second_hand_mall.View.myRefreshLayout) findViewById(R.id.myRefreshLayout);
        progressBar = (ProgressBar) findViewById(R.id.load_progress);

        initBmob.initMyBmob(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //头像点击事件
        icon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //已登录就跳转到信息界面，否则跳转到登录界面
                if(!Utils.signIn) {
                    Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, userInfo.class);
                    startActivity(intent);
                }
            }
        });


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //NavigationView事件处理
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(false) {//!Utils.signIn && !(item.getItemId() == R.id.classify)
                    Toast.makeText(getBaseContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent i;
                switch (item.getItemId()){
                    case R.id.classify:
                        final Classify_Dialog classify_dialog = new Classify_Dialog(MainActivity.this);
                        classify_dialog.show();
                        classify_dialog.setClickListener(new Classify_Dialog.ClickListenerInterface() {
                            @Override
                            public void doclassify(String category, String classify) {
                                search_category = category;
                                search_classify = classify;
                                classify_dialog.dismiss();
                                spList.clear();
                                qgList.clear();
                                newSkip = 0;
                                Utils.getMainList(progressBar,handler,search_name,search_minprice,search_maxprice,search_category,search_classify,sellOrBuy, newSkip);
                            }
                        });
                        break;
                    case R.id.buy:
                        i = new Intent(MainActivity.this, BuyActivity.class);
                        startActivity(i);
                        break;
                    case R.id.sale:
                        i = new Intent(MainActivity.this, SellActivity.class);
                        startActivity(i);
                        break;
                    case R.id.put_out:
                        Utils.m = true;
                        i = new Intent(MainActivity.this, MysellActivity.class);
                        startActivity(i);
                        break;
                    case R.id.put_in:
                        Utils.m = true;
                        i = new Intent(MainActivity.this, MybuyActivity.class);
                        startActivity(i);
                        break;
//                    case R.id.heart:
//                        break;
//                    case R.id.set:
//                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //浮动按钮设置事件
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Search_Dialog search_dialog = new Search_Dialog(MainActivity.this);
                search_dialog.show();
                search_dialog.setClicklistener(new Search_Dialog.ClickListenerInterface() {
                    @Override
                    public void dosearch(String name, String maxprice, String minprice) {
                        if(name != null){
                            search_name = name;
                        }
                        if(minprice != null && maxprice != null){
                            search_minprice = minprice;
                            search_maxprice = maxprice;
                        }
                        search_dialog.dismiss();
                        spList.clear();
                        qgList.clear();
                        newSkip = 0;
                        Utils.getMainList(progressBar,handler,search_name,search_minprice,search_maxprice,search_category,search_classify,sellOrBuy, newSkip);
                    }
                });
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 2:
                        return false;

                    case 3:
                        Utils.signIn = true;
                        icon_text.setText(Utils.currentUser.getUsername());
                        if(!Utils.fileIsExists(Utils.iconPath)) {
                            Utils.downloadIcon(Utils.currentUser.getUser_icon(), handler);
                        }
                        else {
                            Bitmap b = BitmapFactory.decodeFile(Utils.iconPath);
                            icon_image.setImageBitmap(b);
                        }
                        return false;

                    case 4:
                        Bitmap bitmap = BitmapFactory.decodeFile(Utils.iconPath);
                        icon_image.setImageBitmap(bitmap);
                        return false;

                    case 5:
                        newSkip += 10;
                        List<sp> temp1 = (List<sp>) msg.obj;
                        if(temp1.size() != 0){
                            if(search_name != ""){
                                List<sp> temp2 = new ArrayList<sp>();
                                for(sp s : temp1){
                                    if(s.getSp_name().contains(search_name)){
                                        temp2.add(s);
                                    }
                                }
                                temp1 = temp2;
                            }
                            if(temp1.size() != 0 && !search_minprice.equals("") && !search_maxprice.equals("")){
                                List<sp> temp2 = new ArrayList<sp>();
                                for(sp s : temp1){
                                    if(Integer.parseInt(s.getPrice()) >= Integer.parseInt(search_minprice) &&
                                            Integer.parseInt(s.getPrice()) <= Integer.parseInt(search_maxprice)){
                                        temp2.add(s);
                                    }
                                }
                                temp1 = temp2;
                            }
                            spList.addAll(temp1);
                            i += temp1.size();
                            spAdapter.notifyDataSetChanged();
                            if(i < 10){
                                Utils.getMainList(progressBar,handler,search_name,search_minprice,search_maxprice,search_category,search_classify,sellOrBuy,newSkip);
                                return false;
                            }
                            i = 0;
                        }
                        else {
                            i = 0;
                            Toast.makeText(getBaseContext(),"没有更多商品了",Toast.LENGTH_SHORT).show();
                            if(spList.size() == 0){
                                recyclerView.removeAllViews();
                            }
                        }
                        myRefreshLayout.setLoading(false);
                        return false;

                    case 6:
                        newSkip += 10;
                        List<qg> temp3 = (List<qg>) msg.obj;
                        if(temp3.size() != 0){
                            if(search_name != ""){
                                List<qg> temp2 = new ArrayList<qg>();
                                for(qg q : temp3){
                                    if(q.getQgsp_name().contains(search_name)){
                                        temp2.add(q);
                                    }
                                }
                                temp3 = temp2;
                            }
                            if(temp3.size() != 0 && !search_minprice.equals("") && !search_maxprice.equals("")){
                                List<qg> temp2 = new ArrayList<qg>();
                                for(qg q : temp3){
                                    if(Integer.parseInt(q.getPrice()) >= Integer.parseInt(search_minprice) &&
                                            Integer.parseInt(q.getPrice()) <= Integer.parseInt(search_maxprice)){
                                        temp2.add(q);
                                    }
                                }
                                temp3 = temp2;
                            }
                            qgList.addAll(temp3);
                            i += temp3.size();
                            qgAdapter.notifyDataSetChanged();
                            if(i < 10){
                                Utils.getMainList(progressBar,handler,search_name,search_minprice,search_maxprice,search_category,search_classify,sellOrBuy,newSkip);
                                return false;
                            }
                            i = 0;
                        }
                        else {
                            i = 0;
                            Toast.makeText(getBaseContext(),"没有更多求购了",Toast.LENGTH_SHORT).show();
                            if(qgList.size() == 0){
                                recyclerView.removeAllViews();
                            }
                        }
                        myRefreshLayout.setLoading(false);
                        return false;
                }
                return false;
            }
        });

        ll_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellOrBuy = true;
                search_name = "";
                search_minprice = "";
                search_maxprice = "";
                search_category = "";
                search_classify = "";
                newSkip = 0;
                spList.clear();
                spAdapter = new spAdapter(spList);
                recyclerView.setAdapter(spAdapter);
                Utils.getMainList(progressBar,handler,search_name,search_minprice,search_maxprice,search_category,search_classify,sellOrBuy,newSkip);
            }
        });

        ll_qg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sellOrBuy = false;
                search_name = "";
                search_minprice = "";
                search_maxprice = "";
                search_category = "";
                search_classify = "";
                newSkip = 0;
                qgList.clear();
                qgAdapter = new qgAdapter(qgList);
                recyclerView.setAdapter(qgAdapter);
                Utils.getMainList(progressBar,handler,search_name,search_minprice,search_maxprice,search_category,search_classify,sellOrBuy, newSkip);
            }
        });

        if(!Utils.signIn) {
            Utils.getCacheUser(getBaseContext());
            if(!Utils.currentUser.getUsername().equals("")){
                Utils.isSignIn(Utils.currentUser.getUsername(), Utils.currentUser.getPassword(), handler);
            }
        }


        //设置一页的最大数量
        myRefreshLayout.setItemCount(10);
        // 手动调用,通知系统去测量
        myRefreshLayout.measure(0, 0);
        myRefreshLayout.setEnabled(false);
        myRefreshLayout.setOnLoadMoreListener(new myRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(sellOrBuy){
                    Utils.getMainList(progressBar,handler,search_name,search_minprice,search_maxprice,search_category,search_classify,sellOrBuy,newSkip);
                }
                else {
                    Utils.getMainList(progressBar,handler,search_name,search_minprice,search_maxprice,search_category,search_classify,sellOrBuy, newSkip);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    //
    @Override
    protected void onStart() {
        super.onStart();
        Utils.m = false;
        i = 0;

        //打开主界面加载商品或求购的信息
        newSkip = 0;
        spList.clear();
        qgList.clear();
        recyclerView.removeAllViews();
        if(sellOrBuy){
            spAdapter = new spAdapter(spList);
            recyclerView.setAdapter(spAdapter);
        }else {
            qgAdapter = new qgAdapter(qgList);
            recyclerView.setAdapter(qgAdapter);
        }

        Utils.getMainList(progressBar,handler,search_name,search_minprice,search_maxprice,search_category,search_classify,sellOrBuy, newSkip);

        //判断是否登录，然后根据之前的登录情况自动登录
        if(Utils.signIn){
            icon_text.setText(Utils.currentUser.getUsername());
            if(Utils.fileIsExists(Utils.iconPath)) {
                Bitmap bitmap = BitmapFactory.decodeFile(Utils.iconPath);
                icon_image.setImageBitmap(bitmap);
            }
            else {
                Utils.downloadIcon(Utils.currentUser.getUser_icon(), handler);
            }
        }
        else {
            icon_text.setText("点击进行登录");
            icon_image.setImageResource(R.drawable.person_outline);
        }
    }
}
