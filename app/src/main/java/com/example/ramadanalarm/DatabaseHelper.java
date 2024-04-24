package com.example.ramadanalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, "ramadan.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement = "CREATE TABLE RAMADAN_TIME (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, iftar_time TEXT, sehri_time TEXT)";

        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RAMADAN_TIME");
        onCreate(db);
    }


    public void addTime(String date, String iftar_time, String sehri_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("date", date);
        cv.put("iftar_time", iftar_time);
        cv.put("sehri_time", sehri_time);

        long result = db.insert("RAMADAN_TIME", null, cv);
        db.close();

//        if (result==-1){
//            Toast.makeText(context,"Failed to insert", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(context,"Data inserted", Toast.LENGTH_SHORT).show();
//        }


    }



    public RamadanTime getTodaysTime(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + "RAMADAN_TIME" +
                " WHERE " + "date" + " = ?" +
                " LIMIT 1";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{date});
        if (cursor.moveToFirst()) {
            RamadanTime ramadanTime = new RamadanTime();
            ramadanTime.setId(cursor.getInt(0));
            ramadanTime.setDate(cursor.getString(1));
            ramadanTime.setIfterTime(cursor.getString(2));
            ramadanTime.setSehriTime(cursor.getString(3));
            cursor.close();
            return ramadanTime;
        }
        else {
            cursor.close();
            return null;
        }
    }



}
