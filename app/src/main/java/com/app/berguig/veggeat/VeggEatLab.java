package com.app.berguig.veggeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.berguig.veggeat.database.VeggEatBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.app.berguig.veggeat.database.VeggEatDbSchema.VeggEatTable;

/**
 * Created by stagiaire on 16/06/2017.
 */

public class VeggEatLab {

    private List<Restaurant> mRestaurants;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    private void veggEatLab (Context context) {
        mContext= context.getApplicationContext();
        mDatabase = new VeggEatBaseHelper(mContext).getWritableDatabase();
        //mRestaurants = new ArrayList<>();
    }

    public void addRestaurant(Restaurant restaurant){
        ContentValues values = getContentValues(restaurant);
        mDatabase.insert(VeggEatTable.NAME, null, values);
    }

    public List<Restaurant> getRestaurants(){
        return new ArrayList<>();
    }

    public Restaurant getRestaurant(UUID id){
        return null;
    }

    public void updateRestaurant(Restaurant restaurant){
        String uuidString = restaurant.getmId().toString();
        ContentValues values = getContentValues(restaurant);

        mDatabase.update(VeggEatTable.NAME, values, VeggEatTable.Cols.UUID + " = ?", new String[] { uuidString});
    }

        private static ContentValues getContentValues(Restaurant restaurant){
            ContentValues values = new ContentValues();
            values.put(VeggEatTable.Cols.UUID, restaurant.getmId().toString());
            values.put(VeggEatTable.Cols.NAME, restaurant.getmName());
            values.put(VeggEatTable.Cols.LAT, restaurant.getmLAT());
            values.put(VeggEatTable.Cols.LONG, restaurant.getmLONG());
            return values;
        }

        private Cursor queryRestaurants(String whereClause, String[] whereArgs){
            Cursor cursor = mDatabase.query(
                    VeggEatTable.NAME,
                    null, //Colums, null selects all colums
                    whereClause,
                    whereArgs,
                    null, //groupBy,
                    null, // having
                    null //orderBy
            );
            return cursor;
        }




}
