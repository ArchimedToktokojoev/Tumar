package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.text.Html;
import android.util.Log;

import com.example.kadyr.tumar.CommonFunctions;
import com.example.kadyr.tumar.DatabaseHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kadyr on 2/13/2018.
 */

public class Client {
    private int id;
    private String name;
    private Bitmap picture;
    private String telephone;

    public Client(int id, String name, Bitmap picture, String telephone){
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.telephone = telephone;
    }

    public int getId(){return this.id;}
    public void setId(int id){this.id = id;}

    public String getName(){return this.name;}
    public void setName(String name){ this.name = name;}

    public Bitmap getPicture(){return this.picture;}
    public void setPicture(Bitmap picture){this.picture = picture;}

    public String getTelephone(){return this.telephone;}
    public void setTelephone(String telephone){this.telephone = telephone;}

    public static List<Client> GetClients()
    {
        String sql = "SELECT * FROM Clients Order by Name";
        return getClientsByQuery(sql);
    }

    public static Client GetClient(int id){
        String sql = "SELECT * FROM Clients where Id=" + String.valueOf(id);
        List<Client> ret = getClientsByQuery(sql);
        if(ret.size()>0)
            return ret.get(0);
        return null;
    }

    public static Client GetClientbyName(String name){
        String sql = "SELECT * FROM Clients Where Name=\"" + name +"\"";
        List<Client> clients = getClientsByQuery(sql);
        return clients.size()==0?null:clients.get(0);
    }

    public static Cursor GetFilteredCursor(String filterKey){
        String sql = (filterKey == null || filterKey.length() == 0) ?
                "select Id _id,* from Clients":
                "select Id _id,* from Clients where name like \"%" + filterKey + "%\"";
        return DatabaseHelper.GetInstance().database.rawQuery(sql, null);
    }

    public static List<Client> GetFilteredArray(String filterKey){
        String sql = (filterKey == null || filterKey.length() == 0) ?
                "select Id _id,* from Clients":
                "select Id _id,* from Clients where name like \"%" + filterKey + "%\"";
        return getClientsByQuery(sql);
    }

    private static List<Client> getClientsByQuery(String sql) {
        List<Client> ret = new ArrayList<Client>();
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(sql, null);
        if(cursor.getCount()!=0){
            do {
                cursor.moveToNext();
                int id = cursor.getInt(cursor.getColumnIndex("Id"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));

                Bitmap theImage=null;
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("Picture"));
                if(blob!=null){
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(blob);
                    theImage= BitmapFactory.decodeStream(imageStream);
                }
                String telephone = cursor.getString(cursor.getColumnIndex("Telephone"));
                ret.add(new Client( id,  name, theImage,telephone));

            }
            while(!cursor.isLast());
        }
        cursor.close();
        return  ret;
    }

    public long InsertWithExist() throws Exception{

        if(name.equals("")){
            throw new Exception("Имя клиента недопустима!");
        }

        Client client = Client.GetClientbyName(name);
        if(client!=null) return client.getId();

        ContentValues cv = new ContentValues();
        cv.put("Name",this.name);
        if(picture!=null) cv.put("Picture", CommonFunctions.BitmapToByteArray(picture));
        cv.put("Telephone",this.telephone);

        return  DatabaseHelper.GetInstance().database.insert("Clients", null, cv);
    }

    public long Insert() throws Exception {

        if(name.equals("")){
            throw new Exception("Имя клиента недопустима!");
        }
        if(Client.GetClientbyName(name)!=null) throw new Exception("Клиент уже существует!");

        ContentValues cv = new ContentValues();
        cv.put("Name",this.name);
        if(picture!=null) cv.put("Picture", CommonFunctions.BitmapToByteArray(picture));
        cv.put("Telephone",this.telephone);

        return  DatabaseHelper.GetInstance().database.insert("Clients", null, cv);
    }



    public long Delete(){

        String whereClause = "Id = ?";
        String[] whereArgs = new String[]{String.valueOf(this.id)};
        return DatabaseHelper.GetInstance().database.delete("Clients", whereClause, whereArgs);
    }

    public long Update(){

        String whereClause = "Id" + "=" + String.valueOf(this.id);
        ContentValues cv = new ContentValues();
        cv.put("Name",this.name);
        cv.put("Picture", CommonFunctions.BitmapToByteArray(picture));
        cv.put("Telephone", this.telephone);

        return DatabaseHelper.GetInstance().database.update("Clients", cv, whereClause, null);
    }
}
