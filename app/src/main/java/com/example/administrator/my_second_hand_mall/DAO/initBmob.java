package com.example.administrator.my_second_hand_mall.DAO;

import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/7/15.
 */

public class initBmob {

    public static void initMyBmob(Context context){
        Bmob.initialize(context,"fcf205ede3932dc044e3e46b48a32fc8");
    }
}
