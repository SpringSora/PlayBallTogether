package com.example.springsora.balltogether.bean;

import java.sql.Timestamp;

/**
 * Created by Springsora on 2016/5/11.
 */
public class Promise {
    private User user;
    private DateBall dateBall;
    private MyOrder myOrder;
    private Timestamp datetime;
    public Promise(User user, DateBall dateBall, MyOrder myOrder,
                   Timestamp datetime) {
        super();
        this.user = user;
        this.dateBall = dateBall;
        this.myOrder = myOrder;
        this.datetime = datetime;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public DateBall getDateBall() {
        return dateBall;
    }
    public void setDateBall(DateBall dateBall) {
        this.dateBall = dateBall;
    }
    public MyOrder getMyOrder() {
        return myOrder;
    }
    public void setMyOrder(MyOrder myOrder) {
        this.myOrder = myOrder;
    }
    public Timestamp getDatetime() {
        return datetime;
    }
    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }
}

