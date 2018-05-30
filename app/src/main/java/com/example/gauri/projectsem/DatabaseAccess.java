package com.example.gauri.projectsem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new com.example.gauri.projectsem.DatabaseOpenHelper(context);
    }


    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public ArrayList<String> getsongs(String name) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM songs", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(cursor.getString(1).equals(name))
                list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public String getsongsurl(String name) {
        String s="";
        Cursor cursor = database.rawQuery("SELECT * FROM songs", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if(cursor.getString(2).equals(name))
            {s=cursor.getString(3);break;}
            cursor.moveToNext();
        }
        cursor.close();
        return s;
    }
}