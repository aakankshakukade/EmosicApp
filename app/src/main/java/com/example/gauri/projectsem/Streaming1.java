package com.example.gauri.projectsem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Streaming1 extends Activity {

    FloatingActionButton fab;
    PlayerService mservice;
    boolean mServiceBound = false;
String em;
    private ServiceConnection msc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayerService.MyBinder myBinder =(PlayerService.MyBinder) iBinder;
            mservice=myBinder.getService();
            mServiceBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            mServiceBound=false;
        }
    };

    private BroadcastReceiver mrecieve=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            boolean isPlaying=i.getBooleanExtra("isPlaying",false);
            Log.i("error","isplaying"+isPlaying);
            flipPlayPause(isPlaying);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_main1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mServiceBound) {
                    mservice.togglePlayer();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            em = extras.getString("url");
            Toast.makeText(this, em, Toast.LENGTH_SHORT).show();
        }
        startStreamingService(em);
//        if (em.equals("Sad")) {
//            startStreamingService("http://data.starmirchi.com/files/sfd204/101963/Tum%20Ho%20(Rockstar)-(MirchiFun.com).mp3");
//        }
//        else if(em.equals("Angry")){
//            startStreamingService("http://data.starmirchi.com/files/sfd274/136740/Give%20Me%20Some%20Sunshine%20-%20Suraj%20Jagan%20And%20Sharman%20Joshi-(MirchiFun.com).mp3");
//        }
//        else if(em.equals("Surprise")){
//            startStreamingService("http://data.starmirchi.com/files/sfd172/85768/Badal%20Pe%20Paon%20Hain-(MirchiFun.com).mp3");
//        }
//        else if(em.equals("Disgust")){
//            startStreamingService("http://data.starmirchi.com/files/sfd274/136740/Give%20Me%20Some%20Sunshine%20-%20Suraj%20Jagan%20And%20Sharman%20Joshi-(MirchiFun.com).mp3");
//        }
//        else if(em.equals("Fear")){
//            startStreamingService("http://newmp3maza.in/siteuploads/files/sfd119/59436/02%20Dil%20Dhadakta%20Hai%20-%20(Male)-(NewMp3Maza.in).mp3");
//        }
//        else if(em.equals("Happy")){
//            startStreamingService("http://data.starmirchi.com/files/sfd318/158639/Hai%20Junoon%20-%20K.k.-(MirchiFun.com).mp3");
//        }
//        else if(em.equals("Neutral")){
//            startStreamingService("http://data.starmirchi.com/files/sfd172/85768/Badal%20Pe%20Paon%20Hain-(MirchiFun.com).mp3");
//        }
//        else if(em.equals("Contempt")){
//            startStreamingService("http://data.starmirchi.com/files/sfd172/85768/Badal%20Pe%20Paon%20Hain-(MirchiFun.com).mp3");
//        }
    }
    private void startStreamingService(String url)
    {
        Intent i=new Intent(this,PlayerService.class);
        i.putExtra("url",url);
        i.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(i);
        bindService(i,msc,Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mServiceBound){
            unbindService(msc);
            mServiceBound=false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mrecieve,new IntentFilter("changePlayButton"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mrecieve);
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        Intent i=new Intent(Streaming1.this,PlayerService.class);
        i.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
    }
    public void flipPlayPause(boolean isPlaying){
        if(isPlaying)
        {
            Toast.makeText(getApplicationContext(),"Play",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Pause",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


