package com.example.springsora.balltogether.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by JJBOOM on 2016/5/6.
 */
public class MyOrder implements Serializable{
    private Integer o_id;
    private User user;
    private BallGround ballGround;
    private Timestamp o_datetime;
    private Integer o_ispay;
    private Integer o_isevaluate;
    private Integer o_isrefund;
    private Float o_price;
    private String o_date;
    private String o_starttime;
    private String o_endtime;
    public Integer getO_id() {
        return o_id;
    }
    public void setO_id(Integer o_id) {
        this.o_id = o_id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public BallGround getBallGround() {
        return ballGround;
    }
    public void setBallGround(BallGround ballGround) {
        this.ballGround = ballGround;
    }
    public Timestamp getO_datetime() {
        return o_datetime;
    }
    public void setO_datetime(Timestamp o_datetime) {
        this.o_datetime = o_datetime;
    }
    public Integer getO_ispay() {
        return o_ispay;
    }
    public void setO_ispay(Integer o_ispay) {
        this.o_ispay = o_ispay;
    }
    public Integer getO_isevaluate() {
        return o_isevaluate;
    }
    public void setO_isevaluate(Integer o_isevaluate) {
        this.o_isevaluate = o_isevaluate;
    }
    public Integer getO_isrefund() {
        return o_isrefund;
    }
    public void setO_isrefund(Integer o_isrefund) {
        this.o_isrefund = o_isrefund;
    }
    public Float getO_price() {
        return o_price;
    }
    public void setO_price(Float o_price) {
        this.o_price = o_price;
    }
    public String getO_date() {
        return o_date;
    }
    public void setO_date(String o_date) {
        this.o_date = o_date;
    }
    public String getO_starttime() {
        return o_starttime;
    }
    public void setO_starttime(String o_starttime) {
        this.o_starttime = o_starttime;
    }
    public String getO_endtime() {
        return o_endtime;
    }
    public void setO_endtime(String o_endtime) {
        this.o_endtime = o_endtime;
    }
    public MyOrder(Integer o_id, User user, BallGround ballGround,
                   Timestamp o_datetime, Integer o_ispay, Integer o_isevaluate,
                   Integer o_isrefund, Float o_price, String o_date,
                   String o_starttime, String o_endtime) {
        super();
        this.o_id = o_id;
        this.user = user;
        this.ballGround = ballGround;
        this.o_datetime = o_datetime;
        this.o_ispay = o_ispay;
        this.o_isevaluate = o_isevaluate;
        this.o_isrefund = o_isrefund;
        this.o_price = o_price;
        this.o_date = o_date;
        this.o_starttime = o_starttime;
        this.o_endtime = o_endtime;
    }



}
