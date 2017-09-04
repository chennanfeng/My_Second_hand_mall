package com.example.administrator.my_second_hand_mall.bmobObject;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/7/30.
 */

public class sp extends BmobObject {

    private String username;
    private String sp_name;
    private String price;
    private String describe;
    private String category;
    private String classify;
    private BmobFile sp_icon;
    private String user_iconUrl;

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getUser_iconUrl() {
        return user_iconUrl;
    }

    public void setUser_iconUrl(String user_iconUrl) {
        this.user_iconUrl = user_iconUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BmobFile getSp_icon() {
        return sp_icon;
    }

    public void setSp_icon(BmobFile sp_icon) {
        this.sp_icon = sp_icon;
    }
}
