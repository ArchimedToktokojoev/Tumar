package com.example.kadyr.tumar;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kadyr on 2/13/2018.
 */

public class CommonFunctions {
    public static byte[] BitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String DateToString(Date dt) {

        return (String.valueOf(dt.getDate()) + "/" + (dt.getMonth() + 1) + "/" + (dt.getYear() + 1900));
    }
    public static Date StringToDate(String dt) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd/MM/yyyy");
        Date date = null;
        try{
            date= format.parse(dt);
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return date ;
    }

    /* DB Functions  */

    public static long DeleteEntityById(String table, int id){

        String whereClause = "Id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return DatabaseHelper.GetInstance().database.delete(table, whereClause, whereArgs);
    }
}
