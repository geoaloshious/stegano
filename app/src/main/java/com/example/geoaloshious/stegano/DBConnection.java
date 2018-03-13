package com.example.geoaloshious.stegano;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Geo Aloshious on 1/19/2018.
 */
public class DBConnection extends SQLiteOpenHelper {


        SQLiteDatabase sqLiteDb;
        DBConnection(Context context)
        {
            super(context,"DB1",null,1);
        }
    public void openConnection()
        {
            sqLiteDb = getWritableDatabase();
        }
        public  void closeConnection()
        {
            sqLiteDb.close();
        }
    public void onCreate(SQLiteDatabase db) {

        String query1 = "create table tbl_stegno(id INTEGER  PRIMARY  KEY AUTOINCREMENT, name  VARCHAR(20)  NOT  NULL, gender VARCHAR(20)  NOT  NULL, dob VARCHAR(20)  NOT  NULL, phone VARCHAR(20)  NOT  NULL, email VARCHAR(20)  NOT  NULL, uname VARCHAR(20)  NOT  NULL,password  VARCHAR(20)  NOT  NULL)";
        db.execSQL(query1);
    }

    public boolean insertData(String query) //to execute INSERT, UPDATE, and 					//DELETE queries
    {
        try
        {
            sqLiteDb.execSQL(query);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    public Cursor selectData(String query)
    {
        Cursor c = sqLiteDb.rawQuery(query,  null);
        return  c;
    }

    @Override
    public   void   onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}


