package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;

import com.example.kadyr.tumar.DatabaseHelper;

import java.util.Date;

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

    Checkining(int id, int idRoom, int idUser, Date dateCheckin, int dayCount, double debt, double sum ){
        Id = id;
        IdRoom = idRoom;
        IdUser = idUser;
        DateCheckin = dateCheckin;
        DayCount = dayCount;
        Debt = debt;
        Sum = sum;
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


    public long Insert(){

        ContentValues cv = new ContentValues();
        cv.put("IdRoom", this.IdRoom);
        cv.put("IdUser", this.IdUser);
        cv.put("DateCheckin",this.DateCheckin.getTime());
        cv.put("DayCount", this.DayCount);
        cv.put("Debt", this.Debt);
        cv.put("Sum", this.Sum);
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

        return DatabaseHelper.GetInstance().database.update("Checkining", cv, whereClause, null);
    }
}
