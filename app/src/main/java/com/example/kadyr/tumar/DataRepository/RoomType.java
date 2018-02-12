package com.example.kadyr.tumar.DataRepository;

import android.database.Cursor;

import com.example.kadyr.tumar.DatabaseHelper;

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
}
