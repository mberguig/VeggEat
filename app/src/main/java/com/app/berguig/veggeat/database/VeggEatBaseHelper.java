package com.app.berguig.veggeat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.berguig.veggeat.database.VeggEatDbSchema.VeggEatTable;

/**
 * Created by stagiaire on 16/06/2017.
 */

public class VeggEatBaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "veggEat.db";

    public VeggEatBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table" + VeggEatTable.NAME + "(" + "_id integer primary key autoincrement, " + VeggEatTable.Cols.UUID + ", " +
             VeggEatTable.Cols.NAME + ", " +
             VeggEatTable.Cols.LAT + ", " +
            VeggEatTable.Cols.LONG + ")"
    );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
