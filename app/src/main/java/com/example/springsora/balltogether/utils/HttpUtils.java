package com.example.springsora.balltogether.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by JJBOOM on 2016/4/24.
 */
public class HttpUtils  {
    public static Bitmap getNetWorkBitmap(String urlString){
        URL imgUrl;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) imgUrl.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            Log.i("HttpUtils","disconnect");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bitmap;
    }
}
