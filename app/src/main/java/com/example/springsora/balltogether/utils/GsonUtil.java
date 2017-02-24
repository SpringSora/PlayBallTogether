package com.example.springsora.balltogether.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by JJBOOM on 2016/4/22.
 */
public class GsonUtil {
    public static <T> T jsonToBean(String json,Type type){
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
    //装换时间
    public static <T> T jsonToBeanDateTime(String json,Type type){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(json, type);
    }

    public static <T> T jsonToBeanDate(String json,Type type){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.fromJson(json, type);
    }
}

