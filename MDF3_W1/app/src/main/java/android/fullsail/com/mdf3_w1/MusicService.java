package android.fullsail.com.mdf3_w1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shaun on 1/11/2015.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

        /*
        // MEDIA STATES
            Idle - MediaPlayer created, no methods called. Must be initialized in order to use.
            Initialized - Data source is set to the media player. Must be prepared in order to play audio.
            Prepared - MediaPlayer has been prepared synchronously or asynchronously and can now play audio. Audio cannot be played until the player has been prepared.
            Started - Audio is playing. Audio can be paused or stopped at this point.
            Paused - Audio is paused. Audio can be resumed (started) or stopped at this point.
            Stopped - Audio is fully stopped. Must prepare the player again before play can resume.
            End - MediaPlayer has been released using the release() method. Player must be recreated in order to use again.

         */


    private static final int FOREGROUND_NOTIFICATION = 0x01001;
    private static final int REQUEST_NOTIFY_LAUNCH = 0x02001;

    MediaPlayer mPlayer;
    boolean mActivityResumed;
    boolean mPrepared;
    boolean isLooping = false;
    int mAudioPosition;
    int trackPosition;
    int mAudioLength;
    boolean mIdle;
    public String band;
    public String song;
    public int art;
    IBinder mBinder = new LocalBinder();



    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }




    @Override
    public void onCreate() {
        super.onCreate();








    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            Log.e("SERVICES", "Started");

            onPlay();





        return Service.START_STICKY;
    }

    protected void onPlay() {


        Song track1 = new Song(
                "Ghost of the Machine",
                "Dragons Make Bad Babysitters",
                ("android.resource://" + getPackageName() + "/" + R.raw.gotmdragons),
                (R.drawable.gotmphoto)
        );

        Song track2 = new Song(
                "Ghost of the Machine",
                "On Letting Go",
                ("android.resource://" + getPackageName() + "/" + R.raw.gotmlettinggo),
                (R.drawable.gotmphoto2)
        );

        Song track3 = new Song(
                "Ghost of the Machine",
                "Baby, There's No Escape Cuz' I Sold the Exit Door",
                ("android.resource://" + getPackageName() + "/" + R.raw.gotmbaby),
                (R.drawable.gotmphoto3)
        );

        Song track4 = new Song(
                "Ghost of the Machine",
                "She's Not Dancing, She's Dying!",
                ("android.resource://" + getPackageName() + "/" + R.raw.gotmdancing),
                (R.drawable.gotmphoto4)
        );



        Song songs[] = {track1, track2, track3, track4};


        if(mPlayer == null) {
            // assign & initiate media player
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            trackPosition = 0;
            mIdle = false;

            try {
                mPlayer.setDataSource(this, Uri.parse(songs[trackPosition].getTrack()));
                mIdle = false;
                Log.i("Initiating", "Track: " + trackPosition);

                onResume();

            } catch(IOException e) {
                e.printStackTrace();
                Log.e("ERROR!", "--PLAYER RELEASED");
                mPlayer.release();
                mPlayer = null;
            }




        }

        else if (mPlayer != null && mIdle != true)
        {
            Log.i("RESUMING", "Idle: " + mIdle);
            onResume();

        }

        else if (mPlayer != null && mIdle == true){
            try {
                mPlayer.setDataSource(this, Uri.parse(songs[trackPosition].getTrack()));
                mIdle = false;
                onResume();
            } catch(IOException e) {
                e.printStackTrace();
                Log.e("ERROR!", "--PLAYER RELEASED");
                mPlayer.release();
                mPlayer = null;
            }
        }



        band = (songs[trackPosition].getArtist());
        song = (songs[trackPosition].getSong());
        art = (songs[trackPosition].getArt());



        // set pending intent
        Intent songIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent =
                PendingIntent.getActivity(this, REQUEST_NOTIFY_LAUNCH, songIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(art);
        builder.setContentTitle(band);
        builder.setContentText(song);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setContentIntent(pIntent);
        startForeground(FOREGROUND_NOTIFICATION, builder.build());




    }

    // ------------ UI CONTROL ------------------
    protected void onResume() {

        mActivityResumed = true;

        if(mPlayer != null && !mPrepared) {

            mPlayer.prepareAsync();
            Log.i("Preparing", "Track: " + trackPosition);

        } else if(mPlayer != null && mPrepared) {

            Log.i("Resuming", "Track: " + trackPosition);

            mPlayer.seekTo(mAudioPosition);
            mPlayer.start();
        }


    }

    protected void onPause() {
        mActivityResumed = false;


        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mAudioPosition = mPlayer.getCurrentPosition();
            stopForeground(true);
        }
    }

    protected void onStop() {

        if(mPlayer != null && mPlayer.isPlaying()) {
            Log.e("MusicPlayer", "Stopped");
            mPlayer.stop();
            mPrepared = false;
            mAudioPosition = 0;
            stopForeground(true);

        }
    }

    public void onDestroy() {
        super.onDestroy();

        if(mPlayer != null) {
            mPlayer.release();
        }
    }

    protected void onNext() {
        if(trackPosition < 3  && isLooping == false)
            {
                onReset();
                trackPosition++;
                onPlay();


            }
        else if(isLooping == true)
        {
            onReset();
            onPlay();
        }
    }

    protected void onPrevious() {
        if(trackPosition > 0 && isLooping == false)
        {
            onReset();
            trackPosition--;
            onPlay();

        }
        else if(isLooping == true)
        {
            onReset();
            onPlay();
        }
    }

    protected void onReset() {
        if(mPlayer != null) {

            Log.e("MusicPlayer", "RESET");
            mAudioPosition = 0;
            mPlayer.reset();
            mPrepared = false;
            mIdle = true;


        }
    }


    // ------------ SETTERS ------------------
    public void setSongPosition(int newPosition){
        if(mPlayer.isPlaying())
        {
            //onReset();
            mAudioPosition = newPosition;
            mPlayer.seekTo(mAudioPosition);
            Log.i("From Seek Change", "Current Position: " + newPosition);
        }
        else if(!mPlayer.isPlaying())
        {
            mAudioPosition = newPosition;
            onPlay();
            Log.i("From Seek Change", "Current Position: " + mAudioPosition);
        }


    }

    public void setLooping(boolean checkLooping){
        isLooping = checkLooping;
        Log.i("LOOP", "Status: " + checkLooping);
    }






    // ------------ GETTERS ------------------
    public Boolean getLooping(){
        return isLooping;
    }

    public int getSongLength() {

        mAudioLength = mPlayer.getDuration();
        return mAudioLength;
    }

    public int getSongPosition(){
        mAudioPosition = mPlayer.getCurrentPosition();
        Log.i("From getSongPosition Method", "Position: " + mAudioPosition);
        return mAudioPosition;
    }

    public String getBand () {


        return band;
    }

    public String getSong () {


        return song;
    }

    public int getArt(){

        return art;
    }



    public boolean isNotNull(){
        if (mPlayer != null)
        {
            return true;

        }
        else
        {
            return false;
        }

    }



    @Override
    public boolean onUnbind(Intent intent) {



        return super.onUnbind(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {



        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
    mPrepared = true;

    if(mPlayer != null && mActivityResumed) {

        mPlayer.seekTo(mAudioPosition);
        mPlayer.start();
    }

        mAudioLength = mPlayer.getDuration();
        Log.e("Preparing", "Track Length: " + mAudioLength);
}

    @Override
    public void onCompletion(MediaPlayer mp) {


        if(isLooping = true)
        {
            onReset();
            onPlay();
        }
        else if (isLooping = false){
            onNext();
            getBand();
            getSong();
            getArt();
        }

    }
}

