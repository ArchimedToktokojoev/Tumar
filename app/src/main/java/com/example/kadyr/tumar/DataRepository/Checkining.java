package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.kadyr.tumar.DatabaseHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kadyr on 2/2/2018.
 */

public class Checkining {
    private int Id;
    private int IdRoom;
    private int IdUser;
    private Date DateCheckin;
    private int DayCount;
    private double Debt;
    private double Sum;
    private Integer IdClient;

    Checkining(int id, int idRoom, int idUser, Date dateCheckin, int dayCount, double debt, double sum, Integer idClient ){
        Id = id;
        IdRoom = idRoom;
        IdUser = idUser;
        DateCheckin = dateCheckin;
        DayCount = dayCount;
        Debt = debt;
        Sum = sum;
        IdClient = idClient;
    }

    public int getId(){ return Id;}
    public void setId(int id) {Id = id;}

    public int getIdRoom(){ return IdRoom;}
    public void setIdRoom(int idRoom) {IdRoom = idRoom;}

    public int getIdUser(){ return IdUser;}
    public void setIdUser(int idUser){ IdUser = idUser;}

    public Date getDateCheckin(){  return DateCheckin;}
    public void setDateCheckin(Date dateCheckin){ DateCheckin = dateCheckin;}

    public int getDayCount(){ return DayCount;}
    public void setDayCount(int dayCount){DayCount = dayCount;}

    public double getDebt(){return Debt;}
    public void setDebt(double debt){ Debt = debt;}

    public double getSum(){return Sum;}
    public void setSum(double sum){ Sum = sum;}

    public Integer getIdClient(){return IdClient;}
    public void setIdClient(Integer idClient){IdClient = idClient;}

    public static Checkining GetLastCheckining(int idCheckinRoom){
        Checkining lastCheckining=null;

        Cursor cursor = DatabaseHelper.GetInstance().database.
                rawQuery("SELECT * FROM Checkinings WHERE IdRoom=" + String.valueOf(idCheckinRoom) +
                        " order by DateCheckin desc limit 1", null);

        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("Id"));
            int idRoom = cursor.getInt(cursor.getColumnIndex("IdRoom"));
            int idUser = cursor.getInt(cursor.getColumnIndex("IdUser"));
            Date dateCheckin = new Date(cursor.getLong(cursor.getColumnIndex("DateCheckin")));
            int dayCount = cursor.getInt(cursor.getColumnIndex("DayCount"));
            double debt = cursor.getDouble(cursor.getColumnIndex("Debt"));
            double sum = cursor.getDouble(cursor.getColumnIndex("Sum"));
            int idClient = cursor.getInt(cursor.getColumnIndex("IdClient"));
            lastCheckining = new Checkining(id, idRoom, idUser, dateCheckin, dayCount, debt, sum, idClient);
        }
        cursor.close();

        return lastCheckining;
    }

    public long Insert(){

        ContentValues cv = new ContentValues();
        cv.put("IdRoom", this.IdRoom);
        cv.put("IdUser", this.IdUser);
        cv.put("DateCheckin",this.DateCheckin.getTime());
        cv.put("DayCount", this.DayCount);
        cv.put("Debt", this.Debt);
        cv.put("Sum", this.Sum);
        cv.put("IdClient",this.IdClient);

        return  DatabaseHelper.GetInstance().database.insert("Checkinings", null, cv);
    }

    public long Delete(){

        String whereClause = "Id = ?";
        String[] whereArgs = new String[]{String.valueOf(this.Id)};
        return DatabaseHelper.GetInstance().database.delete("Checkinings", whereClause, whereArgs);
    }

    public long Update(){

        String whereClause = "Id" + "=" + String.valueOf(this.Id);
        ContentValues cv = new ContentValues();
        cv.put("IdRoom", this.IdRoom);
        cv.put("IdUser", this.IdUser);
        cv.put("DateCheckin",this.DateCheckin.getTime());
        cv.put("DayCount", this.DayCount);
        cv.put("Debt", this.Debt);
        cv.put("Sum", this.Sum);
        cv.put("IdClient",this.IdClient);

        return DatabaseHelper.GetInstance().database.update("Checkinings", cv, whereClause, null);
    }
}
