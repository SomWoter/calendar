package com.example.projectagendak.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectagendak.Calendar.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventDao
{
    private DatabaseHelper dbHelper;
    public EventDao(Context context)
    {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertarEvento(Event event)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_NAME_TITLE, event.getName());
        values.put(EventContract.EventEntry.COLUMN_NAME_DATE, event.getDate().toString());
        values.put(EventContract.EventEntry.COLUMN_NAME_TIME, event.getTime().toString());

        long id = db.insert(EventContract.EventEntry.TABLE_NAME, null, values);
        db.close();

        return id;
    }
    public ArrayList<Event> getEventsForDateAndTime(LocalDate date, LocalTime time) {
        ArrayList<Event> events = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_NAME_TITLE,
                EventContract.EventEntry.COLUMN_NAME_DATE,
                EventContract.EventEntry.COLUMN_NAME_TIME
        };

        String selection = EventContract.EventEntry.COLUMN_NAME_DATE + " = ? AND " +
                EventContract.EventEntry.COLUMN_NAME_TIME + " = ?";
        String[] selectionArgs = { date.toString(), time.toString() };

        Cursor cursor = db.query(
                EventContract.EventEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(EventContract.EventEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_TITLE));
            String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_DATE));
            String timeStr = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_TIME));

            Event event = new Event(name, LocalDate.parse(dateStr), LocalTime.parse(timeStr));
            event.setId(id);

            events.add(event);
        }

        cursor.close();
        db.close();

        return events;
    }


    public int actualizarEvento(Event event)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_NAME_TITLE, event.getName());
        values.put(EventContract.EventEntry.COLUMN_NAME_DATE, event.getDate().toString());
        values.put(EventContract.EventEntry.COLUMN_NAME_TIME, event.getTime().toString());

        String selection = EventContract.EventEntry._ID + " =?";
        String[] selectionArgs = {String.valueOf(event.getId())};

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
        return count;
    }

    public int eliminarEvento(long eventId)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = EventContract.EventEntry._ID + " =?";
        String[] selectionArgs = {String.valueOf(eventId)};

        int deletedRows = db.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        return deletedRows;
    }

    public void eliminarTodosLosEventos() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(EventContract.EventEntry.TABLE_NAME, null, null);
        db.close();
    }

    public ArrayList<Event> obtenerTodosLosEventos()
    {
        ArrayList<Event> events = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_NAME_TITLE,
                EventContract.EventEntry.COLUMN_NAME_DATE,
                EventContract.EventEntry.COLUMN_NAME_TIME
        };

        Cursor cursor = db.query(
                EventContract.EventEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext())
        {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(EventContract.EventEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_TIME));

            Event event = new Event(name, LocalDate.parse(date), LocalTime.parse(time));
            event.setId(id);

            events.add(event);
        }

        cursor.close();
        db.close();

        return events;
    }

    public ArrayList<Event> obtenerEventosPorFecha(LocalDate selectedDate) {
        ArrayList<Event> events = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_NAME_TITLE,
                EventContract.EventEntry.COLUMN_NAME_DATE,
                EventContract.EventEntry.COLUMN_NAME_TIME
        };

        String selection = EventContract.EventEntry.COLUMN_NAME_DATE + " = ?";
        String[] selectionArgs = { selectedDate.toString() };

        Cursor cursor = db.query(
                EventContract.EventEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(EventContract.EventEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(EventContract.EventEntry.COLUMN_NAME_TIME));

            Event event = new Event(name, LocalDate.parse(date), LocalTime.parse(time));
            event.setId(id);

            events.add(event);
        }

        cursor.close();
        db.close();

        return events;
    }
}
