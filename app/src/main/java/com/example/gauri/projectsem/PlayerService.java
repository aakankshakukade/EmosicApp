package com.example.gauri.projectsem;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;


public class PlayerService extends Service {
    MediaPlayer media = new MediaPlayer();
    private final IBinder ib=new MyBinder();


    public class MyBinder extends Binder
    {
        PlayerService getService()
        {

            return PlayerService.this;
        }
    }
    public PlayerService() {
    }

    public int onStartCommand(Intent i, int flag, int startId){
        if(i.getStringExtra("url")!=null)
            playStream(i.getStringExtra("url"));

        if(i.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)){
            Log.i("info","Start foreground service");
            showNotification();
        }
        else if(i.getAction().equals(Constants.ACTION.PREV_ACTION)){
            Log.i("info","Previous Pressed");
        }
        else if(i.getAction().equals(Constants.ACTION.PLAY_ACTION)){
            Log.i("info","Play Pressed");
            togglePlayer();
        }
        else if(i.getAction().equals(Constants.ACTION.NEXT_ACTION)){
            Log.i("info","Next Pressed");
        }
        else if(i.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)){
            Log.i("info","Stoped foreground service");
            stopForeground(true);
            stopSelf();
        }

        return START_STICKY;
    }

    private void showNotification() {
        Intent ni = new Intent(this,MainActivity.class);
        ni.setAction(Constants.ACTION.MAIN_ACTION);
        //ni.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(this, 0, ni, 0);

        Intent previ = new Intent(this,PlayerService.class);
        previ.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent pi1 = PendingIntent.getActivity(this, 0, previ, 0);

        Intent playi = new Intent(this, PlayerService.class);
        playi.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pi2 = PendingIntent.getActivity(this, 0, playi, 0);

        Intent nexti = new Intent(this, PlayerService.class);
        nexti.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pi3 = PendingIntent.getActivity(this, 0, nexti, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.elogo);

        int playPause=android.R.drawable.ic_media_play;
        if(media!=null && media.isPlaying()){
            playPause=android.R.drawable.ic_media_pause;
        }

        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle("Emosic")
                .setTicker("Playing Music")
                .setContentText("Song")
                .setSmallIcon(R.drawable.playpause)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pi)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_previous, "Previous", pi1)
                .addAction(playPause,"Play/Pause", pi2)
                .addAction(android.R.drawable.ic_media_next,"Next", pi3)
                .build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,noti);
    }
    public void playStream(String url) {

        if (media != null) {
            try {
                media.start();
            } catch (Exception e) {

            }
            media = null;
        }
        media = new MediaPlayer();
        media.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            media.setDataSource(url);
            media.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    playPlayer();
                }
            });
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    flipPlayPause(false);
                }
            });
            media.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void pausePlayer() {
        try {
            media.pause();
            flipPlayPause(false);
            showNotification();
            unregisterReceiver(noisy);
        } catch (Exception e) {
            Log.d("Exception", "failed to pause player");
        }
    }

    public void playPlayer() {
        try {
            getAudioFocusPlay();
            flipPlayPause(true);
            showNotification();
        } catch (Exception e) {
            Log.d("Exception", "failed to play player");
        }
    }

    public void flipPlayPause(boolean isPlaying)
    {
        Intent i=new Intent("changePlayButton");
        i.putExtra("isPlaying",isPlaying);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
    public void togglePlayer() {
        try {
            if (media.isPlaying()) {
                pausePlayer();
            } else {
                playPlayer();
            }
        } catch (Exception e) {
            Log.d("Exception", "failed to toggle");
        }
    }
    private AudioManager am;
    private Boolean playBeforeInterruption=false;

    public void getAudioFocusPlay()
    {
        am= (AudioManager) this.getBaseContext().getSystemService(Context.AUDIO_SERVICE);


        int result=am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if(result== AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            media.start();
            registerReceiver(noisy,ifilter);
        }
    }
    AudioManager.OnAudioFocusChangeListener afChangeListener= new AudioManager.OnAudioFocusChangeListener(){

        @Override
        public void onAudioFocusChange(int i) {
            if(i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                if(media.isPlaying()){
                    playBeforeInterruption=true;
                }else{
                    playBeforeInterruption=false;
                }
                pausePlayer();
            }
            else if(i== AudioManager.AUDIOFOCUS_GAIN){
                if(playBeforeInterruption)
                    playPlayer();
            }
            else if(i== AudioManager.AUDIOFOCUS_LOSS){
                pausePlayer();
                am.abandonAudioFocus(afChangeListener);
            }
        }
    };
    @Override
    public IBinder onBind(Intent i) {
        // TODO: Return the communication channel to the service.
            return ib;
    }

    private class Noisy extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())){
                Log.i("HEADPHONE","UNPLUGGED");
                pausePlayer();
            }
        }
    }

    private IntentFilter ifilter= new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private Noisy noisy=new Noisy();
}
