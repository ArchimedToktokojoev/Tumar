package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;

import com.example.kadyr.tumar.DatabaseHelper;

/**
 * Created by Kadyr on 2/2/2018.
 */

public class Payment {
    private int Id;
    private double Sum;
    private int IdUser;
    private int IdRoom;
    private long Date;
    private int OperationType;
    private int IdCheckining;


    public Payment(int id, double sum, int idUser, int idRoom, long date, int operationType, int idCheckining){
        Id = id;
        Sum = sum;
        IdUser = idUser;
        IdRoom = idRoom;
        Date = date;
        OperationType = operationType;
        IdCheckining = idCheckining;
    }

    public int getId(){ return Id;}
    public void setId(int id) {Id=id;}

    public double getSum(){ return Sum;}
    public void setSum(double sum){ Sum =sum;}

    public int getIdUser(){ return IdUser;}
    public void setIdUser(int idUser){ IdUser = idUser; }

    public int getIdRoom(){ return IdRoom;}
    public void setIdRoom(int idRoom){ IdRoom = idRoom;}

    public long getDate(){ return Date;}
    public void setDate(long date){ Date= date;}

    public int getOperationType(){ return OperationType;}
    public void setOperationType(int operationType){ OperationType = operationType;}

    public int getIdCheckining(){return IdCheckining;}
    public void setIdCheckining(int idCheckining){ IdCheckining = idCheckining;}


    public long Insert(){

        ContentValues cv = new ContentValues();
        cv.put("Sum",this.Sum);
        cv.put("IdUser", this.IdUser);
        cv.put("IdRoom", this.IdRoom);
        cv.put("Date", this.Date);
        cv.put("OperationType", this.OperationType);
        cv.put("IdCheckining", this.IdCheckining);

        return  DatabaseHelper.GetInstance().database.insert("Payments", null, cv);
    }

    public long Delete(){

        String whereClause = "Id = ?";
        String[] whereArgs = new String[]{String.valueOf(this.Id)};
        return DatabaseHelper.GetInstance().database.delete("Payments", whereClause, whereArgs);
    }

    public long Update(){

        String whereClause = "Id" + "=" + String.valueOf(this.Id);
        ContentValues cv = new ContentValues();
        cv.put("Sum",this.Sum);
        cv.put("IdUser", this.IdUser);
        cv.put("IdRoom", this.IdRoom);
        cv.put("Date", this.Date);
        cv.put("OperationType", this.OperationType);


        return DatabaseHelper.GetInstance().database.update("Payments", cv, whereClause, null);
    }

    public long UpdatePrice(){

        
        String whereClause = "Id" + "=" + String.valueOf(this.Id);
        ContentValues cv = new ContentValues();
        cv.put("Sum",this.Sum);

        return DatabaseHelper.GetInstance().database.update("Payments", cv, whereClause, null);
    }

}
