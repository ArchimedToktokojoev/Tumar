package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kadyr.tumar.Constants;
import com.example.kadyr.tumar.DatabaseHelper;
import com.example.kadyr.tumar.PublicVariables;

import java.time.Period;
import java.time.temporal.ChronoUnit;
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
    private Date DateCheckOut;

    public Room(int id, String name, int status, int idRoomType, int idRoomGroup, Date dateCheckout){
        Id = id;
        Name = name;
        Status = status;
        IdRoomType = idRoomType;
        IdRoomGroup=idRoomGroup;
        DateCheckOut = dateCheckout;
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

    private Date getDateCheckOut() { return DateCheckOut;}
    private void setDateCheckOut(Date dateCheckOut){ this.DateCheckOut = dateCheckOut;}

    public int GetBedsCount() {
        return RoomType.GetRoomType(IdRoomType).getBedsCount();
    }
    public long CheckOutDays() {

        return ((DateCheckOut.getTime()-System.currentTimeMillis())/(24*60*60*1000)+1);
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
            Date dateCheckout = new Date(cursor.getLong(cursor.getColumnIndex("DateCheckout")));
            ret =  new Room( id,  name,  status, idRoomType,  idRoomGroup, dateCheckout);
        }
        cursor.close();
        return  ret;
    }

    public Client GetClient(){
        if(Status==Constants.RoomStatusFree) return null;
        Client client = null;


        Cursor cursor = DatabaseHelper.GetInstance().database.
                rawQuery("SELECT IdClient FROM Checkinings Where IdRoom="+String.valueOf(this.Id) +
                        " order by DateCheckin desc limit 1", null);

        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex("IdClient"));
            client = Client.GetClient(id);
         }
        cursor.close();

        return client;
    }

    public void DoCheckin(Date dateCheckin, int dayCount, double sum, double paid, String nameClient ) throws Exception
    {
        Integer idClient=null;

        nameClient = nameClient.trim();
        if(!nameClient.equals("")){
            try{
                Client newClient = new Client(0,nameClient,null,"");
                idClient = (int)newClient.InsertWithExist();
            }catch (Exception ex){
                throw ex;
            }
        }

        Checkining newCheckining = new Checkining(0, this.Id, PublicVariables.CurrentUser.GetId(), dateCheckin, dayCount, sum-paid, sum, idClient);
        newCheckining.setId((int)newCheckining.Insert());

        if(paid!=0)
        {
            Payment newPayment = new Payment(0, paid, PublicVariables.CurrentUser.GetId(), this.Id, dateCheckin.getTime(), Constants.PaymentForCheckining, newCheckining.getId());
            newPayment.Insert();
        }

        ContentValues cv = new ContentValues();
        cv.put("Status", Constants.RoomStatusBusy);
        cv.put("DateCheckout",dateCheckin.getTime() + dayCount*24*60*60*1000);
        DatabaseHelper.GetInstance().database.
        update("Rooms", cv, "Id=" + String.valueOf(Id), null);
    }

    public void EditCheckin(int idCheckining, Date dateCheckin, int dayCount, double sum, double paid, String nameClient ) throws Exception
    {
        try{
            DatabaseHelper.GetInstance().database.beginTransaction();

            Cursor cursorForPaymentsId = DatabaseHelper.GetInstance().database.
                    rawQuery("select Id from Payments where IdCheckining=" + String.valueOf(idCheckining) + " limit 1", null);

            int idEditingPayment=0;
            if(cursorForPaymentsId.getCount()!=0){
                cursorForPaymentsId.moveToFirst();
                idEditingPayment = cursorForPaymentsId.getInt(cursorForPaymentsId.getColumnIndex("Id"));
            }

                Payment editingPayment = new Payment(idEditingPayment, paid, PublicVariables.CurrentUser.GetId(), this.Id, dateCheckin.getTime(), Constants.PaymentForCheckining, idCheckining);
                editingPayment.UpdatePrice();
                Log.e("axa1", String.valueOf(editingPayment.getSum()));

            Integer idClient=null;
            nameClient = nameClient.trim();
            if(!nameClient.equals("")){
                try{
                    Client newClient = new Client(0,nameClient,null,"");
                    idClient = (int)newClient.InsertWithExist();
                }catch (Exception ex){
                    throw ex;
                }
            }

            Checkining editingCheckining = new Checkining(idCheckining, this.Id, PublicVariables.CurrentUser.GetId(), dateCheckin, dayCount, sum-paid, sum, idClient);
            editingCheckining.Update();


            ContentValues cv = new ContentValues();
            cv.put("Status", Constants.RoomStatusBusy);
            cv.put("DateCheckout",dateCheckin.getTime() + dayCount*24*60*60*1000);
            DatabaseHelper.GetInstance().database.
                    update("Rooms", cv, "Id=" + String.valueOf(Id), null);
            DatabaseHelper.GetInstance().database.setTransactionSuccessful();
        }
        catch(Exception ex){
            throw ex;
        }
        finally {
            DatabaseHelper.GetInstance().database.endTransaction();
        }

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

    private static Room cursorToRoom(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex("Id"));
        String name = cursor.getString(cursor.getColumnIndex("Name"));
        int status= cursor.getInt(cursor.getColumnIndex("Status"));
        int idRoomType = cursor.getInt(cursor.getColumnIndex("IdRoomType"));
        int idRoomGroup = cursor.getInt(cursor.getColumnIndex("IdRoomGroup"));
        Date dateCheckout = new Date(cursor.getLong(cursor.getColumnIndex("DateCheckout")));
        return new Room( id,  name,  status, idRoomType,  idRoomGroup, dateCheckout);
    }

    @NonNull
    private static List<Room> getRooms(String sql) {
        List<Room> ret = new ArrayList<Room>();
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(sql, null);
        if(cursor.getCount()!=0){
            do {
                cursor.moveToNext();
                Log.d("DateCheckout", String.valueOf(cursor.getColumnIndex("DateCheckout")));
                int id = cursor.getInt(cursor.getColumnIndex("Id"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                int status= cursor.getInt(cursor.getColumnIndex("Status"));
                int idRoomType = cursor.getInt(cursor.getColumnIndex("IdRoomType"));
                int idRoomGroup = cursor.getInt(cursor.getColumnIndex("IdRoomGroup"));
                Date dateCheckout = new Date(cursor.getLong(cursor.getColumnIndex("DateCheckout")));

                ret.add(new Room( id,  name,  status, idRoomType,  idRoomGroup, dateCheckout));

            }
            while(!cursor.isLast());
        }
        cursor.close();
        return  ret;
    }


}
