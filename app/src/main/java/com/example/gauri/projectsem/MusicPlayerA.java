package com.example.gauri.projectsem;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MusicPlayerA extends Activity {
    ArrayList<String> em;

    DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
       Bundle extras = getIntent().getExtras();
        if (extras != null) {
            em = extras.getStringArrayList("Songslist");
            //Toast.makeText(this, em, Toast.LENGTH_SHORT).show();
        }



        final ListView listview = (ListView) findViewById(R.id.listview);

        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();



            //ArrayList<String> list= databaseAccess.getsongs(em);

            databaseAccess.close();


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, em);


        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);//get url

                databaseAccess.open();
                String s = databaseAccess.getsongsurl(item);
                Intent i = new Intent(MusicPlayerA.this, Streaming1.class);
                i.putExtra("url", s);
                startActivity(i);

            }

            ;
        });


    }
}



