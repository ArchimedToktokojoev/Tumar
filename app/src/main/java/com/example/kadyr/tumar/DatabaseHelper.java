package com.example.kadyr.tumar;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * Created by Kadyr on 1/31/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    public static String DB_PATH; // полный путь к базе данных
    public static String DB_NAME = "Tumar.db";
    private static final int SCHEMA = 1; // версия базы данных
    private Context myContext;
    private static DatabaseHelper instance = null;
    public SQLiteDatabase database;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext=context;
        DB_PATH =context.getFilesDir().getPath() + "/" + DB_NAME;
    }



    public static DatabaseHelper GetInstance(Context context){
        if(instance==null){
            instance=new DatabaseHelper(context);
        }
        return instance;
    }

    public static DatabaseHelper GetInstance(){
        return instance;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
    }

    void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {

                this.getReadableDatabase();

                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();

            }
            else {
              // file.delete();
              //  Log.d("Axa", "FileDeleted");
            }
        }
        catch(Exception ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
        database = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

}
