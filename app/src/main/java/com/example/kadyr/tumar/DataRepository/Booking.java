package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.example.kadyr.tumar.DatabaseHelper;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Kadyr on 2/5/2018.
 */

public class Booking {
    private int id;
    private int idRoom;
    private Date dateFrom;
    private Date dateTo;
    private Date dateCreated;
    private double deposit;

    public Booking(int id, int idRoom, Date dateFrom, Date dateTo, Date dateCreated, double deposit ){
        this.id = id;
        this.idRoom = idRoom;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.dateCreated = dateCreated;
        this.deposit = deposit;
    }

    public int GetId(){ return id;}
    public void SetId(int id){ this.id = id;}

    public int GetIdRoom() { return idRoom;}
    public void SetIdRoom(int idRoom){ this.idRoom = idRoom;}

    public Date GetDateFrom() { return dateFrom;}
    public void SetDateFrom(Date dateFrom){ this.dateFrom = dateFrom;}

    public Date GetDateTo(){ return dateTo;}
    public void SetDateTo(Date dateTo){ this.dateTo = dateTo;}

    public Date GetDateCreated(){ return dateCreated;}
    public void SetDateCreated(Date dateCreated){this.dateCreated = dateCreated;}

    public double GetDeposit(){ return deposit;}
    public void SetDeposit(double deposit){ this.deposit = deposit;}



    public static long GetCount(){
        return DatabaseUtils.queryNumEntries(DatabaseHelper.GetInstance().database, "Bookings");
    }

    public static Booking GetBooking(int id){
        Booking booking = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?","Bookings", "Id");
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            int idRoom = cursor.getInt(cursor.getColumnIndex("IdRoom"));
            Date dateFrom = new Date(cursor.getLong(cursor.getColumnIndex("DateFrom")));
            Date dateTo = new Date(cursor.getLong(cursor.getColumnIndex("DateTo"))) ;
            Date dateCreated = new Date(cursor.getLong(cursor.getColumnIndex("DateCreated"))) ;
            Double deposit = cursor.getDouble(cursor.getColumnIndex("Deposit"));
            booking=new Booking(id, idRoom, dateFrom, dateTo, dateCreated, deposit);
        }
        cursor.close();
        return  booking;
    }

    public long Insert(Booking booking){

        ContentValues cv = new ContentValues();
        cv.put("IdRoom", booking.GetIdRoom());
        cv.put("DateFrom", booking.GetDateFrom().getTime());
        cv.put("DateTo", booking.GetDateTo().getTime());
        cv.put("DateCreated", booking.GetDateCreated().getTime());
        cv.put("Deposit",booking.GetDeposit());

        return  DatabaseHelper.GetInstance().database.insert("Users", null, cv);
    }

    public long Delete(long bookingId){

        String whereClause = "Id = ?";
        String[] whereArgs = new String[]{String.valueOf(bookingId)};
        return DatabaseHelper.GetInstance().database.delete("Users", whereClause, whereArgs);
    }

    public long Update(Booking booking){

        String whereClause = "Id" + "=" + String.valueOf(booking.GetId());
        ContentValues cv = new ContentValues();
        cv.put("IdRoom", booking.GetIdRoom());
        cv.put("DateFrom", booking.GetDateFrom().getTime());
        cv.put("DateTo", booking.GetDateTo().getTime());
        cv.put("DateCreated", booking.GetDateCreated().getTime());
        cv.put("Deposit",booking.GetDeposit());

        return DatabaseHelper.GetInstance().database.update("Bookings", cv, whereClause, null);
    }

}
