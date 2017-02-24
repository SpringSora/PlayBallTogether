package com.example.springsora.balltogether.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by JJBOOM on 2016/4/29.
 */
public class BitmapUtils {
    public static Bitmap scaleBitmap(Bitmap src){
        int width = src.getWidth();
        int height = src.getHeight();
        final float destSize = 500;

        float scaleWidth =destSize/width;
        float scaleHeight = destSize/height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        return Bitmap.createBitmap(src,0,0,width,height,matrix,true);
    }
}
