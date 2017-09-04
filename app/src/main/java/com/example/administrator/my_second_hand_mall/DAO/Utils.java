package com.example.administrator.my_second_hand_mall.DAO;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.my_second_hand_mall.R;
import com.example.administrator.my_second_hand_mall.bmobObject.qg;
import com.example.administrator.my_second_hand_mall.bmobObject.sp;
import com.example.administrator.my_second_hand_mall.bmobObject.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/7/21.
 */

public class Utils {

    public static user currentUser = new user();
    public static String iconPath = "";
    public static boolean signIn = false;
    public static boolean m = false;//控制商品和求购适配器的加载模式
    public static boolean canDel = false;//控制删除按钮，和选择图标的出现
    public static List<qg> qgList_del = new ArrayList<qg>();
    public static List<sp> spList_del = new ArrayList<sp>();
    public static List<String> iconUrl = new ArrayList<String>();

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）/^0?1[3|4|5|7|8][0-9]\d{8}$/
        总结起来就是第一位必定为1，第二位必定为3或5或8或7（电信运营商），其他位置的可以为0-9
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    //验证密码是否相等
    public static boolean isPwdEqual(EditText t1, EditText t2){
        if(t1.getText().toString().equals(t2.getText().toString())){
            return true;
        }
        else
            return false;
    }

    /**验证用户名是否存在
     因为Bmob的存储和读取是异步操作所以需要用到Handle处理消息
     */
    public static void isIdExist(String s, final Handler handler){
        BmobQuery<user> query = new BmobQuery<user>();
        query.addWhereEqualTo("username", s);
        query.setLimit(1);
        query.findObjects(new FindListener<user>() {
            @Override
            public void done(List<user> list, BmobException e) {
                if(e == null){
                    Message message = new Message();
                    message.what = 0;
                    message.obj = list.get(0);
                    handler.sendMessage(message);
                }
                else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }

        });
    }

    //已发布的商品数如果大于等于10就无法继续发布商品
    public static void isFabu(final Handler handler){
        BmobQuery<sp> query = new BmobQuery<sp>();
        query.addWhereEqualTo("username", Utils.currentUser.getUsername());
        query.setLimit(10);
        query.count(sp.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    if(integer < 10){
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                    else {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            }
        });
    }

    //已发布的求购数如果大于等于10就无法继续发布求购
    public static void isqgFabu(final Handler handler){
        BmobQuery<qg> query = new BmobQuery<qg>();
        query.addWhereEqualTo("username", Utils.currentUser.getUsername());
        query.setLimit(10);
        query.count(qg.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    if(integer < 10){
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                    else {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }
            }
        });
    }

    /**验证用户名是否存在
     因为Bmob的存储和读取是异步操作所以需要用到Handle处理消息
     */
    public static void isSignIn(String id, String pwd, final Handler handler){
        //条件1
        BmobQuery<user> eq1 = new BmobQuery<user>();
        eq1.addWhereEqualTo("username", id);

        //条件2
        BmobQuery<user> eq2 = new BmobQuery<user>();
        eq2.addWhereEqualTo("password", pwd);

        //组合条件
        List<BmobQuery<user>> querys = new ArrayList<BmobQuery<user>>();
        querys.add(eq1);
        querys.add(eq2);


        BmobQuery<user> query = new BmobQuery<user>();
        query.and(querys);
        query.setLimit(1);
        query.findObjects(new FindListener<user>() {
            @Override
            public void done(List<user> list, BmobException e) {
                if(e == null){
                    Utils.currentUser.setObjectId(list.get(0).getObjectId());
                    Utils.currentUser.setUsername(list.get(0).getUsername());
                    Utils.currentUser.setPassword(list.get(0).getPassword());
                    Utils.currentUser.setQuestion(list.get(0).getQuestion());
                    Utils.currentUser.setMobilePhoneNumber(list.get(0).getMobilePhoneNumber());
                    Utils.currentUser.setAnswer(list.get(0).getAnswer());
                    Utils.currentUser.setUser_icon(list.get(0).getUser_icon());
                    if(Utils.iconPath.equals("")) {
                        Utils.iconPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/" + list.get(0).getUser_icon().getFilename();
                    }

                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);
                }
                else {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        });
    }


    //用来保存登录过的用户
    public static void cacheUser(Context context, user u){
        SharedPreferences.Editor editor = context.getSharedPreferences("User",Context.MODE_PRIVATE).edit();
        editor.putString("username",u.getUsername());
        editor.putString("password",u.getPassword());
        editor.putString("question",u.getQuestion());
        if(u.getUser_icon().getLocalFile() != null) {
            editor.putString("user_icon", u.getUser_icon().getLocalFile().getAbsolutePath());
        }
        else {
            editor.putString("user_icon", Utils.iconPath);
        }
        editor.apply();
    }

    //获得缓存用户的信息
    public static void getCacheUser(Context context){
        SharedPreferences pref = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        currentUser.setUsername(pref.getString("username",""));
        currentUser.setPassword(pref.getString("password",""));
        currentUser.setQuestion(pref.getString("question",""));
        iconPath = pref.getString("user_icon", "");
        if(fileIsExists(iconPath)) {
            currentUser.setUser_icon(new BmobFile(new File(iconPath)));
        }
    }

    //设置当前登录用户信息
    public static void setCurrentUser(user u){
        currentUser.setUsername(u.getUsername());
        currentUser.setPassword(u.getPassword());
        currentUser.setQuestion(u.getQuestion());
        currentUser.setUser_icon(u.getUser_icon());
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try
        {
            File f = new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public static void downloadIcon(BmobFile bmobFile, final Handler handler){
        File saveFile = new File(Utils.iconPath);
        bmobFile.download(saveFile, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null) {
                    Message message = new Message();
                    message.what = 4;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
    }

    public static void myBuyList(final ProgressBar progressBar, final Handler handler){
        progressBar.setVisibility(View.VISIBLE);
        BmobQuery<qg> bmobQuery = new BmobQuery<qg>();
        bmobQuery.addWhereEqualTo("username",Utils.currentUser.getUsername());
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<qg>() {
            @Override
            public void done(List<qg> list, BmobException e) {
                if(e == null){
                    progressBar.setVisibility(View.GONE);
                    Message message = new Message();
                    message.what = 0;
                    message.obj = list;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public static void mySellList(final ProgressBar progressBar, final Handler handler){
        progressBar.setVisibility(View.VISIBLE);
        BmobQuery<sp> bmobQuery = new BmobQuery<sp>();
        bmobQuery.addWhereEqualTo("username",Utils.currentUser.getUsername());
        bmobQuery.setLimit(10);
        bmobQuery.findObjects(new FindListener<sp>() {
            @Override
            public void done(List<sp> list, BmobException e) {
                if(e == null){
                    progressBar.setVisibility(View.GONE);
                    Message message = new Message();
                    message.what = 0;
                    message.obj = list;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public static void qgList_delete(List<qg> qgs, final Handler handler){
        List<BmobObject> myQgs = new ArrayList<BmobObject>();
        for (qg q : qgs){
            myQgs.add(q);
        }
        new BmobBatch().deleteBatch(myQgs).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if(e == null){
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
                else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public static void spList_delete(List<sp> sps, final Handler handler){
        List<BmobObject> mySps = new ArrayList<BmobObject>();
        for (sp s : sps){
            mySps.add(s);
        }
        new BmobBatch().deleteBatch(mySps).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if(e == null){
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
                else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public static void getMainList(final ProgressBar progressBar, final Handler handler, String search_name, String search_minprice,
                                   String search_maxprice, String search_category, String search_classify, boolean sellOrBuy, int newSkip){
        progressBar.setVisibility(View.VISIBLE);
        if(sellOrBuy){
            //获取商品列表
            BmobQuery<sp> bmobQuery = new BmobQuery<sp>();
            bmobQuery.setLimit(10);
            bmobQuery.setSkip(newSkip);
            if(!search_category.equals("")){
                bmobQuery.addWhereEqualTo("category", search_category);
                bmobQuery.addWhereEqualTo("classify", search_classify);
            }
            bmobQuery.findObjects(new FindListener<sp>() {
                @Override
                public void done(List<sp> list, BmobException e) {
                    if(e == null){
                        progressBar.setVisibility(View.GONE);
                        Message message = new Message();
                        message.what = 5;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                }
            });
        }
        else {
            //获取求购列表
            BmobQuery<qg> bmobQuery = new BmobQuery<qg>();
            bmobQuery.setLimit(10);
            bmobQuery.setSkip(newSkip);
            if(!search_category.equals("")){
                bmobQuery.addWhereEqualTo("category", search_category);
                bmobQuery.addWhereEqualTo("classify", search_classify);
            }
            bmobQuery.findObjects(new FindListener<qg>() {
                @Override
                public void done(List<qg> list, BmobException e) {
                    if(e == null){
                        progressBar.setVisibility(View.GONE);
                        Message message = new Message();
                        message.what = 6;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                }
            });
        }
    }

    public static void updateUserInfo(user u , final Handler handler){

        u.update(u.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public static List<String> getStringArray(Context context , int arrayResource){
        Resources resources = context.getResources();
        String[] s = resources.getStringArray(arrayResource);
        List<String> temp = Arrays.asList(s);
        return temp;
    }
}
