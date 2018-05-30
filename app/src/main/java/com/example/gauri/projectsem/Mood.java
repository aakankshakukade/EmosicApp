package com.example.gauri.projectsem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Mood extends Activity {

    ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        iv1 = (ImageView) findViewById(R.id.imageView1);
        final String status = "happy";
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jump = new Intent(Mood.this, MusicPlayerB.class);
                jump.putExtra("emotion", status);
                startActivity(jump);
            }
        });
        iv2 = (ImageView) findViewById(R.id.imageView2);
        final String status1 = "sad";
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jump = new Intent(Mood.this, MusicPlayerB.class);
                jump.putExtra("emotion", status1);
                startActivity(jump);
            }
        });
        iv3 = (ImageView) findViewById(R.id.imageView3);
        final String status2 = "fear";
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jump = new Intent(Mood.this, MusicPlayerB.class);
                jump.putExtra("emotion", status2);
                startActivity(jump);
            }
        });
        iv4 = (ImageView) findViewById(R.id.imageView4);
        final String status3 = "anger";
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jump = new Intent(Mood.this, MusicPlayerB.class);
                jump.putExtra("emotion", status3);
                startActivity(jump);
            }
        });
        iv5 = (ImageView) findViewById(R.id.imageView5);
        final String status4 = "contempt";
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jump = new Intent(Mood.this, MusicPlayerB.class);
                jump.putExtra("emotion", status4);
                startActivity(jump);
            }
        });
        iv6 = (ImageView) findViewById(R.id.imageView6);
        final String status5 = "disgust";
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jump = new Intent(Mood.this, MusicPlayerB.class);
                jump.putExtra("emotion", status5);
                startActivity(jump);
            }
        });
        iv7 = (ImageView) findViewById(R.id.imageView7);
        final String status6 = "surprise";
        iv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jump = new Intent(Mood.this, MusicPlayerB.class);
                jump.putExtra("emotion", status6);
                startActivity(jump);
            }
        });
        iv8 = (ImageView) findViewById(R.id.imageView8);
        final String status7 = "contempt";
        iv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jump = new Intent(Mood.this, MusicPlayerB.class);
                jump.putExtra("emotion", status7);
                startActivity(jump);
            }
        });
    }
}