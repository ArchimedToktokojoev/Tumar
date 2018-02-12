package com.example.kadyr.tumar.DataRepository;

/**
 * Created by Kadyr on 2/2/2018.
 */

public class Checkining {
    private int Id;
    private int IdRoom;
    private int IdUser;
    private long Date;

    Checkining(int id, int idRoom, int idUser, long date ){
        Id = id;
        IdRoom = idRoom;
        IdUser = idUser;
        Date = date;
    }

    public int getId(){ return Id;}
    public void setId(int id) {Id = id;}

    public int getIdRoom(){ return IdRoom;}
    public void setIdRoom(int idRoom) {IdRoom = idRoom;}

    public int getIdUser(){ return IdUser;}
    public void setIdUser(int idUser){ IdUser = idUser;}

    public long getDate(){  return Date;}
    public void setDate(long date){ Date = date;}

}
