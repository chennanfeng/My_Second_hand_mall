package com.example.administrator.my_second_hand_mall.bmobObject;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/7/15.
 */

public class user extends BmobObject {
    private String username;
    private String password;
    private String mobilePhoneNumber;
    private String question;
    private String answer;
    private BmobFile user_icon;

    public BmobFile getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(BmobFile user_icon) {
        this.user_icon = user_icon;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
