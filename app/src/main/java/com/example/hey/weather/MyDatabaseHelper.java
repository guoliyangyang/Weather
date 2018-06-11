package com.example.hey.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Hey on 2017/4/9.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USERS = "create table WEATHER(" +
            "USER CHAR(20) PRIMARY KEY NOT NULL," +
            "PASSWORD CHAR(15) NOT NULL," +
            "EMAIL CHAR(50) NOT NULL," +
            "PROVINCE CHAR(50)," +
            "CITY CHAR(50)," +
            "COUNTY CHAR(50)," +
            "ADDRESS CHAR(150) )";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
        Toast.makeText(mContext, "database create success", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists WEATHER");
        onCreate(db);
    }
}
