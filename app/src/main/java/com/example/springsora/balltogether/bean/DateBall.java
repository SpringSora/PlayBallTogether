package com.example.springsora.balltogether.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Springsora on 2016/4/27.
 */

public class DateBall  implements  Serializable {
    private Integer d_id;
    private User user;
    private String d_pic;
    private String d_title;
    private String d_text;
    private int d_type;
    private String d_location;
    private int d_num;
    private Timestamp d_time;
    private String d_date;
    private String d_starttime;
    private String d_endtime;
    private BallGround ballGround;
    private String d_city;
    public DateBall(User user, String d_pic, String d_title, String d_text,
                    int d_type, String d_location, int d_num, String d_date,
                    String d_starttime, String d_endtime, BallGround ballGround,String d_city) {
        super();
        this.user = user;
        this.d_pic = d_pic;
        this.d_title = d_title;
        this.d_text = d_text;
        this.d_type = d_type;
        this.d_location = d_location;
        this.d_num = d_num;
        this.d_date = d_date;
        this.d_starttime = d_starttime;
        this.d_endtime = d_endtime;
        this.ballGround = ballGround;
        this.d_city = d_city;
    }
    public Integer getD_id() {
        return d_id;
    }
    public void setD_id(Integer d_id) {
        this.d_id = d_id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getD_pic() {
        return d_pic;
    }
    public void setD_pic(String d_pic) {
        this.d_pic = d_pic;
    }
    public String getD_title() {
        return d_title;
    }
    public void setD_title(String d_title) {
        this.d_title = d_title;
    }
    public String getD_text() {
        return d_text;
    }
    public void setD_text(String d_text) {
        this.d_text = d_text;
    }
    public int getD_type() {
        return d_type;
    }
    public void setD_type(int d_type) {
        this.d_type = d_type;
    }

    public String getD_city() {
        return d_city;
    }

    public void setD_city(String d_city) {
        this.d_city = d_city;
    }

    public String getD_location() {
        return d_location;
    }

    public void setD_location(String d_location) {
        this.d_location = d_location;
    }

    public int getD_num() {
        return d_num;
    }
    public void setD_num(int d_num) {
        this.d_num = d_num;
    }

    public Timestamp getD_time() {
        return d_time;
    }

    public void setD_time(Timestamp d_time) {
        this.d_time = d_time;
    }

    public String getD_date() {
        return d_date;
    }
    public void setD_date(String d_date) {
        this.d_date = d_date;
    }
    public String getD_starttime() {
        return d_starttime;
    }
    public void setD_starttime(String d_starttime) {
        this.d_starttime = d_starttime;
    }
    public String getD_endtime() {
        return d_endtime;
    }
    public void setD_endtime(String d_endtime) {
        this.d_endtime = d_endtime;
    }
    public BallGround getBallGround() {
        return ballGround;
    }
    public void setBallGround(BallGround ballGround) {
        this.ballGround = ballGround;
    }


}

