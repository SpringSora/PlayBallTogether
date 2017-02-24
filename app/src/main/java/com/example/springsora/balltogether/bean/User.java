package com.example.springsora.balltogether.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by JJBOOM on 2016/4/22.
 */
public class User implements Serializable{
    private Integer u_id;
    private String u_phone;
    private String u_password;
    private String u_nickname;
    private String u_pic;
    private String u_sex;
    private Integer u_age;
    private String signature;
    private String u_playtype;
    private String u_name;

    public User(Integer u_id, String u_phone, String u_password,
                String u_nickname, String u_pic, String u_sex, Integer u_age,
                String signature, String u_playtype, String u_name) {
        super();
        this.u_id = u_id;
        this.u_phone = u_phone;
        this.u_password = u_password;
        this.u_nickname = u_nickname;
        this.u_pic = u_pic;
        this.u_sex = u_sex;
        this.u_age = u_age;
        this.signature = signature;
        this.u_playtype = u_playtype;
        this.u_name = u_name;
    }
    public Integer getU_id() {
        return u_id;
    }
    public void setU_id(Integer u_id) {
        this.u_id = u_id;
    }
    public String getU_phone() {
        return u_phone;
    }
    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }
    public String getU_password() {
        return u_password;
    }
    public void setU_password(String u_password) {
        this.u_password = u_password;
    }
    public String getU_nickname() {
        return u_nickname;
    }
    public void setU_nickname(String u_nickname) {
        this.u_nickname = u_nickname;
    }
    public String getU_pic() {
        return u_pic;
    }
    public void setU_pic(String u_pic) {
        this.u_pic = u_pic;
    }
    public String getU_sex() {
        return u_sex;
    }
    public void setU_sex(String u_sex) {
        this.u_sex = u_sex;
    }
    public Integer getU_age() {
        return u_age;
    }
    public void setU_age(Integer u_age) {
        this.u_age = u_age;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getU_playtype() {
        return u_playtype;
    }
    public void setU_playtype(String u_playtype) {
        this.u_playtype = u_playtype;
    }
    public String getU_name() {
        return u_name;
    }
    public void setU_name(String u_name) {
        this.u_name = u_name;
    }
}