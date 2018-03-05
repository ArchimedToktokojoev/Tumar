package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.kadyr.tumar.CommonFunctions;
import com.example.kadyr.tumar.DatabaseHelper;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Kadyr on 2/22/2018.
 */

public class RoomPrice {
    public int Id;
    public Date DateOperation;
    public double Price;
    public int IdRoomType;

    public RoomPrice(int id, Date dateOperation, double price, int idRoomType){
        this.Id=id;
        this.DateOperation = dateOperation;
        this.Price = price;
        this.IdRoomType = idRoomType;
    }

    public long Insert(){

        ContentValues cv = new ContentValues();
        cv.put("DateOperation", this.DateOperation.getTime());
        cv.put("Price", this.Price);
        cv.put("IdRoomType", this.IdRoomType);

        return  DatabaseHelper.GetInstance().database.insert("RoomPrices", null, cv);
    }

    public static long Insert(String dateOperation, String price, String roomTypeName){
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd/MM/yyyy");
        Date dt = null;
        try{
            dt= format.parse(dateOperation);
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
        RoomType rt = RoomType.GetRoomTypeByName(roomTypeName);
        if(rt==null)
            return -1;
     //       throw new Exception("Тип комнаты не определен") ;
        RoomPrice rp = new RoomPrice(0,dt,Double.valueOf(price),rt.getId()) ;
        return rp.Insert();


    }

    private static List<RoomPrice> getDataByQuery(String sql) {
        List<RoomPrice> ret = new ArrayList<RoomPrice>();
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(sql, null);
        if(cursor.getCount()!=0){
            do {
                cursor.moveToNext();
                ret.add(new RoomPrice( cursor.getInt(cursor.getColumnIndex("Id")),
                                       new Date(cursor.getLong(cursor.getColumnIndex("DateOperation"))),
                                       cursor.getDouble(cursor.getColumnIndex("Price")),
                                       cursor.getInt(cursor.getColumnIndex("IdRoomType"))));
            }
            while(!cursor.isLast());
        }
        cursor.close();
        return  ret;
    }


    public static  List<RoomPrice> GetRoomPrices()        {

        return getDataByQuery("SELECT * FROM RoomPrices order by DateOperation , IdRoomType desc");
    }

    public static  RoomPrice GetActualRoomPrice(int idRoomType, Date dt)        {

        String date = String.valueOf(dt.getTime());
        List<RoomPrice> data = getDataByQuery("SELECT * FROM RoomPrices \n" +
                "where IdRoomType="+String.valueOf(idRoomType)+"  and DateOperation <="+date+
                " order by DateOperation desc limit 1");
        return data.size()==0?null:data.get(0);
    }


    public static  RoomPrice GetRoomPrice(int id)        {

        String sql = "Select * from RoomPrices where Id=" + String.valueOf(id);
        List<RoomPrice> data = getDataByQuery(sql);
        return data.size()==0?null:data.get(0);
    }


    public long Delete(){

        return CommonFunctions.DeleteEntityById("RoomPrices", this.Id);
    }

    public long Update(){

        String whereClause = "Id" + "=" + String.valueOf(this.Id);
        ContentValues cv = new ContentValues();
        cv.put("DateOperation", this.DateOperation.getTime());
        cv.put("Price", this.Price);
        cv.put("IdRoomType", this.IdRoomType);

        return DatabaseHelper.GetInstance().database.update("RoomPrices", cv, whereClause, null);
    }


    public long UpdateWithParams(String dateOperation, String price, String roomTypeName) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd/MM/yyyy");
        Date dt = null;
        try {
            dt = format.parse(dateOperation);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        RoomType rt = RoomType.GetRoomTypeByName(roomTypeName);
        if (rt == null)
            throw new Exception("Тип комнаты не определен");
        this.IdRoomType = rt.getId();
        this.DateOperation = dt;
        this.Price = Double.valueOf(price);



        return this.Update();

    }

    }
