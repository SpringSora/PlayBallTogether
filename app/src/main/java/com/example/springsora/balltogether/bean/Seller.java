package com.example.springsora.balltogether.bean;

import java.io.Serializable;

/**
 * Created by JJBOOM on 2016/4/29.
 */
public class Seller implements Serializable {
    private int seller_id;
    private String account;
    private String password;

    public Seller(int seller_id, String account, String password) {
        super();
        this.seller_id = seller_id;
        this.account = account;
        this.password = password;
    }

    public Seller(String account, String password) {
        super();
        this.account = account;
        this.password = password;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

