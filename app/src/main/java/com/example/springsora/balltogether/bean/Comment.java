package com.example.springsora.balltogether.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Springsora on 2016/5/9.
 */
public class Comment implements Serializable{
    private Integer c_id;
    private User user;
    private BallGround ballGround;
    private Timestamp datetime;
    private String c_content;
    public Comment(Integer c_id, User user, BallGround ballGround,
                   Timestamp datetime, String c_content) {
        super();
        this.c_id = c_id;
        this.user = user;
        this.ballGround = ballGround;
        this.datetime = datetime;
        this.c_content = c_content;
    }

    public Integer getC_id() {
        return c_id;
    }
    public void setC_id(Integer c_id) {
        this.c_id = c_id;
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
    public Timestamp getDatetime() {
        return datetime;
    }
    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }
    public String getC_content() {
        return c_content;
    }
    public void setC_content(String c_content) {
        this.c_content = c_content;
    }


}

