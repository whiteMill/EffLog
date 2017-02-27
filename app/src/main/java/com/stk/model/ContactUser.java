package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2016/11/2.
 */

public class ContactUser implements Serializable{
    private String username;
    private String user_id;
    private String firstCharacter;
    private String phone;
    private Boolean isCheck;
    private String peo_type;


    public ContactUser() {
    }

    public ContactUser(String username, String user_id, String firstCharacter, String phone, Boolean isCheck) {
        this.username = username;
        this.user_id = user_id;
        this.firstCharacter = firstCharacter;
        this.phone = phone;
        this.isCheck = isCheck;
    }

    public String getPeo_type() {
        return peo_type;
    }

    public void setPeo_type(String peo_type) {
        this.peo_type = peo_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirstCharacter() {
        return firstCharacter;
    }

    public void setFirstCharacter(String firstCharacter) {
        this.firstCharacter = firstCharacter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }



}
