package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kadyr.tumar.Constants;
import com.example.kadyr.tumar.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Kadyr on 2/2/2018.
 */

public class Room {
    private int Id;
    private String Name;
    private int Status;
    private int IdRoomType;
    private int IdRoomGroup;

    public Room(int id, String name, int status, int idRoomType, int idRoomGroup){
        Id = id;
        Name = name;
        Status = status;
        IdRoomType = idRoomType;
        IdRoomGroup=idRoomGroup;
    }

    public int getId(){return Id;}
    public void setId(int id){Id=id;}

    public String getName(){ return Name;}
    public void setName(String name) { Name = name;}

    public int getStatus(){ return Status;}
    public void setStatus(int status){ Status = status;}

    public int getIdRoomType(){ return IdRoomType;}
    public void setIdRoomType(int idRoomType){ IdRoomType = idRoomType;}

    public int getIdRoomGroup(){ return IdRoomGroup;}
    public void setIdRoomGroup(int idRoomGroup){ IdRoomGroup=idRoomGroup;}

    public int GetBedsCount() {
        return RoomType.GetRoomType(IdRoomType).getBedsCount();
    }
    public int CheckOutDays() {
        return new Random().nextInt(30)+1;
    }


    public static List<Room> GetRooms(int idRoomGroup){

        String sql = "SELECT * FROM Rooms WHERE IdRoomGroup="+String.valueOf(idRoomGroup)+" Order by Name";
        return getRooms(sql);
    }

    public boolean IsBooked(){
        Long now = System.currentTimeMillis()/1000;
        String sql = "select Count(*) from Bookings B" +
                " where B.DateFrom >= " + String.valueOf(now)+" and B.IdRoom="+String.valueOf(Id);
        Cursor cursor =  DatabaseHelper.GetInstance().database.rawQuery(sql,null);
        cursor.moveToFirst();
        return (cursor.getInt(0)>0);
    }


    public static Room GetRoom(int id){
       Room ret = null ;
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery("SELECT * FROM Rooms where id="+String.valueOf(id), null);
        if(cursor.getCount()!=0){
                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                int status= cursor.getInt(cursor.getColumnIndex("Status"));
                int idRoomType = cursor.getInt(cursor.getColumnIndex("IdRoomType"));
                int idRoomGroup = cursor.getInt(cursor.getColumnIndex("IdRoomGroup"));
                ret = new Room( id,  name,  status, idRoomType,  idRoomGroup);

        }
        cursor.close();
        return  ret;
    }

    public boolean CheckIn(int idUser, Date dateOper,int daysCount, double summa, double smpayed, int idClient){

        ContentValues cv = new ContentValues();
        cv.put("Status", Constants.RoomStatusBusy);
        return DatabaseHelper.GetInstance().database.
                update("Rooms", cv, "Id=" + String.valueOf(Id), null)>=0;

    }
    public boolean CheckOut(int idUser, Date dateOper){

        ContentValues cv = new ContentValues();
        cv.put("Status", Constants.RoomStatusFree);
        return DatabaseHelper.GetInstance().database.
                update("Rooms", cv, "Id=" + String.valueOf(Id), null)>=0;

    }

    public static List<Room> GetRooms(){
        return getRooms("SELECT * FROM Rooms Order by Name");
    }

    @NonNull
    private static List<Room> getRooms(String sql) {
        List<Room> ret = new ArrayList<Room>();
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(sql, null);
        if(cursor.getCount()!=0){
            do {
                cursor.moveToNext();
                int id = cursor.getInt(cursor.getColumnIndex("Id"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                int status= cursor.getInt(cursor.getColumnIndex("Status"));
                int idRoomType = cursor.getInt(cursor.getColumnIndex("IdRoomType"));
                int idRoomGroup = cursor.getInt(cursor.getColumnIndex("IdRoomGroup"));
                ret.add(new Room( id,  name,  status, idRoomType,  idRoomGroup));

            }
            while(!cursor.isLast());
        }
        cursor.close();
        return  ret;
    }


}
