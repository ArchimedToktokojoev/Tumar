package com.example.kadyr.tumar.DataRepository;

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

    public Payment(int id, double sum, int idUser, int idRoom, long date, int operationType){
        Id = id;
        Sum = sum;
        IdUser = idUser;
        IdRoom = idRoom;
        Date = date;
        OperationType = operationType;
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


}
