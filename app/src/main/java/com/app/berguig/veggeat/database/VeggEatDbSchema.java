package com.app.berguig.veggeat.database;

/**
 * Created by stagiaire on 16/06/2017.
 */

public class VeggEatDbSchema {
    public static final class VeggEatTable {
        public static final String NAME = "restaurants";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String LAT = "lat";
            public static final String LONG = "long";
        }
    }
}
