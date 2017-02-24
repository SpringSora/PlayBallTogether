package com.example.springsora.balltogether.utils;

/**
 * Created by JJBOOM on 2016/5/5.
 */
public class Distance {

    public static double distance(double lng1,double lat1,double lng2,double lat2){
        double a, b ,R;
        R = 6378137; // µØÇò°ë¾¶
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (lng1 - lng2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));
        return d;
    }
}
