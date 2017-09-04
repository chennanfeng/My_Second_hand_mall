package com.example.administrator.my_second_hand_mall.bmobObject;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/8/1.
 */

public class qg extends BmobObject {

    private String username;
    private String qgsp_name;
    private String price;
    private String describe;
    private String category;
    private String classify;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQgsp_name() {
        return qgsp_name;
    }

    public void setQgsp_name(String qgsp_name) {
        this.qgsp_name = qgsp_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
