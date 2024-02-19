package com.example.projectagendak.DataBase;

import android.provider.BaseColumns;

public class EventContract {
    private EventContract(){}

    public static class EventEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
    }
}
