package com.styleshow.data;

// I am not sure if this is in correct package
// maybe data.contract

import android.provider.BaseColumns;

public class DatabaseContract {

    private DatabaseContract() {}

    // TODO: all tables

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

}
