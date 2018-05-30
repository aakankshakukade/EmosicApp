package com.example.gauri.projectsem;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerB extends Activity {
    SQLiteDatabase eventsDB;
    Cursor c;
    DatabaseAccess databaseAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player_b);
        String em = "happy";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            em = extras.getString("emotion");
            Toast.makeText(this, em, Toast.LENGTH_SHORT).show();
        }
         databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();


               try {
       //c=eventsDB.rawQuery("SELECT * FROM songs",null);
      ArrayList<String> list= databaseAccess.getsongs(em);

            databaseAccess.close();

            Intent playback = new Intent(this, MusicPlayerA.class);
           playback.putExtra("Songslist", list);
           startActivity(playback);


        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
