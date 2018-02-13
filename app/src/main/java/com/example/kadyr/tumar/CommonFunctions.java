package com.example.kadyr.tumar;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by Kadyr on 2/13/2018.
 */

public class CommonFunctions {
    public static byte[] BitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
