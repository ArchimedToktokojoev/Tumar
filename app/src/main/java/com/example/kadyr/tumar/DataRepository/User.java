package com.example.kadyr.tumar.DataRepository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.kadyr.tumar.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kadyr on 1/31/2018.
 */

public class User {
    private int id;
    private String name;
    private String login;
    private String password;
    private int idUserGroup;

    public User(int id, int idUserGroup, String name, String login, String password){
        this.id = id;
        this.idUserGroup = idUserGroup;
        this.name = name;
        this.login = login;
        this.password = password;
    }
    public int GetId(){
        return this.id;
    }
    public void SetId(int id){
        this.id=id;
    }

    public int GetIdUserGroup(){
        return idUserGroup;
    }
    public void SetIdUserGroup(int idUserGroup){
        this.idUserGroup=idUserGroup;
    }

    public String GetName(){
        return name;
    }
    public void SetName(String name){
        this.name=name;
    }

    public String GetLogin(){
        return login;
    }
    public void SetLogin(String login){
        this.login = login;
    }

    public String GetPassword(){
        return password;
    }
    public void SetPassword(String password){
        this.password=password;
    }

    public static List<User> GetUsers(){
        ArrayList<User> users = new ArrayList<>();

        try {

            String[] columns = new String[]{"Id", "IdUserGroup", "Name", "Login", "Password"};

            //Cursor cursor = database.rawQuery("select * from Users", null);
            Cursor cursor = DatabaseHelper.GetInstance().database.query("Users", columns, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("Id"));
                    int idUserGroup = cursor.getInt(cursor.getColumnIndex("IdUserGroup"));
                    String name = cursor.getString(cursor.getColumnIndex("Name"));
                    String login = cursor.getString(cursor.getColumnIndex("Login"));
                    String password = cursor.getString(cursor.getColumnIndex("Password"));
                    users.add(new User(id, idUserGroup, name, login, password));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            Log.e("Msg","getUsers OK");
        } catch(Exception ex){
            Log.e("Msg",ex.getMessage());
        }
        return  users;
    }

    public long GetCount(){
        return DatabaseUtils.queryNumEntries(DatabaseHelper.GetInstance().database, "Users");
    }

    public User GetUser(int id){
        User user = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?","Users", "Id");
        Cursor cursor = DatabaseHelper.GetInstance().database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            int idUserGroup = cursor.getInt(cursor.getColumnIndex("IdUserGroup"));
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String login = cursor.getString(cursor.getColumnIndex("Login"));
            String password = cursor.getString(cursor.getColumnIndex("Password"));
            user = new User(id, idUserGroup, name,login,password);
        }
        cursor.close();
        return  user;
    }

    public long Insert(User user){

        ContentValues cv = new ContentValues();
        cv.put("Name", user.GetName());
        cv.put("IdUserGroup", user.GetIdUserGroup());
        cv.put("Login", user.GetLogin());
        cv.put("Password", user.GetPassword());

        return  DatabaseHelper.GetInstance().database.insert("Users", null, cv);
    }

    public long Delete(long userId){

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};
        return DatabaseHelper.GetInstance().database.delete("Users", whereClause, whereArgs);
    }

    public long Update(User user){

        String whereClause = "Id" + "=" + String.valueOf(user.GetId());
        ContentValues cv = new ContentValues();
        cv.put("Name", user.GetName());
        cv.put("IdUserGroup", user.GetIdUserGroup());
        cv.put("Login", user.GetLogin());
        cv.put("Password", user.GetPassword());
        return DatabaseHelper.GetInstance().database.update("Users", cv, whereClause, null);
    }



}
