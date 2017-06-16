package com.app.berguig.veggeat.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.app.berguig.veggeat.Restaurant;

/**
 * Created by stagiaire on 16/06/2017.
 */

public class VeggEatCursorWrapper extends CursorWrapper {
    public VeggEatCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Restaurant getRestaurant(){
        String uuidString=getString(getColumnIndex(VeggEatDbSchema.VeggEatTable.Cols.UUID));
        String name=getString(getColumnIndex(VeggEatDbSchema.VeggEatTable.Cols.NAME));
        long lat = getLong(getColumnIndex(VeggEatDbSchema.VeggEatTable.Cols.LAT));
        long lon = getLong(getColumnIndex(VeggEatDbSchema.VeggEatTable.Cols.LONG));

        return null;
    }
}
