package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.kadyr.tumar.Constants;
import com.example.kadyr.tumar.DatabaseHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kadyr on 2/2/2018.
 */

public class RoomGroup {
    private int id;
    private String name;

    public RoomGroup(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int GetId(){ return id;}
    public void SetId(int id) {this.id = id;}

    public String GetName(){ return name;}
    public void SetName(String name) { this.name = name;}

    public static int getFreePlaces(int idGroup)  {
        Cursor cursor =  DatabaseHelper.GetInstance().database.rawQuery("select sum(RT.BedsCount) as Sum from Rooms R\n" +
                "inner join RoomTypes RT on RT.Id=R.IdRoomType \n" +
                "where R.Status="+ Constants.RoomStatusFree+" and R.IdRoomGroup="+idGroup,null);
        if(cursor.getCount()==0) return 0;
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    public static long GetCount(int idGroup){
        Cursor cursor =  DatabaseHelper.GetInstance().database.rawQuery("select sum(RT.BedsCount) as Sum from Rooms R\n" +
                "inner join RoomTypes RT on RT.Id=R.IdRoomType "+
                "where R.IdRoomGroup="+idGroup+"\n",null);
        if(cursor.getCount()==0) return 0;
        cursor.moveToFirst();

        return cursor.getInt(0);

    }


    public static boolean IsExistBookedRoom(int idRoomGroup){
        Long now = System.currentTimeMillis()/1000;
        String sql = "select Count(*) from Rooms R" +
                " inner join RoomTypes RT on RT.Id=R.IdRoomType" +
                " inner join Bookings BK on BK.IdRoom=R.Id" +
                " where BK.DateFrom >= " + String.valueOf(now)+" and R.IdRoomGroup="+String.valueOf(idRoomGroup);
        Cursor cursor =  DatabaseHelper.GetInstance().database.rawQuery(sql,null);
        cursor.moveToFirst();
        return (cursor.getInt(0)>0);
    }


    public static List<RoomGroup> GetRoomGroups(){
        List<RoomGroup> ret = new ArrayList<RoomGroup>();
        String query = "SELECT * FROM RoomGroups";
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(query, null);
        if(cursor.getCount()!=0){
            do {
                cursor.moveToNext();
                int id = cursor.getInt(cursor.getColumnIndex("Id"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                ret.add(new RoomGroup(id, name));
            }
            while(!cursor.isLast());
        }
        cursor.close();
        return  ret;
    }




}
