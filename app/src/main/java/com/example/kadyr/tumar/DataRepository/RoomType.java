package com.example.kadyr.tumar.DataRepository;

import android.database.Cursor;
import android.util.Log;

import com.example.kadyr.tumar.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kadyr on 2/2/2018.
 */

public class RoomType {
    private int Id;
    private String Name;
    private int BedsCount;

    public RoomType(int id, String name, int bedsCount){
        Id = id;
        Name = name;
        BedsCount = bedsCount;
    }

    public int getId(){ return Id;}
    public void setId(int id) { Id = id;}

    public String getName(){ return Name;}
    public void setName(String name){ Name = name;}

    public int getBedsCount(){ return BedsCount;}
    public void setBedsCount(int bedsCount){ BedsCount = bedsCount; }

    public static  RoomType GetRoomType(int id)        {
        RoomType roomType = null ;
        String sql = "Select * from RoomTypes where Id="+String.valueOf(id);
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(sql, null);
        if(cursor.moveToFirst()) {
            roomType = new RoomType(
                    cursor.getInt(cursor.getColumnIndex("Id")),
                    cursor.getString(cursor.getColumnIndex("Name")),
                    cursor.getInt(cursor.getColumnIndex("BedsCount")));

        }
        cursor.close();
        return  roomType ;

    }
    public static  RoomType GetRoomTypeByName(String name) {
        RoomType roomType = null ;
        String sql = "Select * from RoomTypes where Name=\""+name+"\"";
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(sql, null);
        if(cursor.moveToFirst()) {
            roomType = new RoomType(
                    cursor.getInt(cursor.getColumnIndex("Id")),
                    cursor.getString(cursor.getColumnIndex("Name")),
                    cursor.getInt(cursor.getColumnIndex("BedsCount")));

        }
        cursor.close();
        return  roomType ;

    }
    public static List<String> GetFilteredArray(String filterKey){
        List<String> ret = new ArrayList<>() ;
        String sql = (filterKey == null || filterKey.length() == 0) ?
                "select Name from RoomTypes":
                "select Name from RoomTypes where name like \"%" + filterKey + "%\"";
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(sql, null);
        if(cursor.getCount()!=0){
            do {
                cursor.moveToNext();
                ret.add(cursor.getString(cursor.getColumnIndex("Name")));
            }
            while(!cursor.isLast());
        }
        cursor.close();

        return ret;
    }

    public static List<RoomType> GetRoomTypes()
    {
        List<RoomType> ret = new ArrayList<>()  ;
        String sql = "Select * from RoomTypes order by Name";
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(sql, null);
        if(cursor.getCount()!=0){
            do {
                cursor.moveToNext();
                ret.add(new RoomType(
                        cursor.getInt(cursor.getColumnIndex("Id")),
                        cursor.getString(cursor.getColumnIndex("Name")),
                        cursor.getInt(cursor.getColumnIndex("BedsCount"))));

            }
            while(!cursor.isLast());
        }
        cursor.close();
        return  ret ;

    }
}
