package android.fullsail.com.mdf3_w1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
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

    private static final String SAVE_POSITION = "MainActivity.SAVE_POSITION";

    MediaPlayer mPlayer;
    boolean mActivityResumed;
    boolean mPrepared;
    int mAudioPosition;
    int songPosition;
    boolean mIdle;
    public String band;
    public String song;
    IBinder mBinder = new LocalBinder();

    public static final int STANDARD_NOTIFICATION = 0x01001;




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
                ("android.resource://" + getPackageName() + "/" + R.raw.gotmdragons)
        );

        Song track2 = new Song(
                "Ghost of the Machine",
                "On Letting Go",
                ("android.resource://" + getPackageName() + "/" + R.raw.gotmlettinggo)
        );

        Song track3 = new Song(
                "Ghost of the Machine",
                "Baby, There's No Escape Cuz' I Sold the Exit Door",
                ("android.resource://" + getPackageName() + "/" + R.raw.gotmbaby)
        );

        Song track4 = new Song(
                "Ghost of the Machine",
                "She's Not Dancing, She's Dying!",
                ("android.resource://" + getPackageName() + "/" + R.raw.gotmdancing)
        );



        Song songs[] = {track1, track2, track3, track4};


        if(mPlayer == null) {
            // assign & initiate media player
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            songPosition = 0;
            mIdle = false;

            try {
                mPlayer.setDataSource(this, Uri.parse(songs[songPosition].getTrack()));
                mIdle = false;
                Log.i("Initiating", "Track: " + songPosition);
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
                mPlayer.setDataSource(this, Uri.parse(songs[songPosition].getTrack()));
                mIdle = false;
                onResume();
            } catch(IOException e) {
                e.printStackTrace();
                Log.e("ERROR!", "--PLAYER RELEASED");
                mPlayer.release();
                mPlayer = null;
            }
        }






        band = (songs[songPosition].getArtist());
        song = (songs[songPosition].getSong());
    }

    protected void onResume() {



        mActivityResumed = true;


        if(mPlayer != null && !mPrepared) {
            Log.i("Preparing", "Track: " + songPosition);
            mPlayer.prepareAsync();
        } else if(mPlayer != null && mPrepared) {
            Log.i("Resuming", "Track: " + songPosition);
            mPlayer.seekTo(mAudioPosition);
            mPlayer.start();
        }
    }

    protected void onPause() {
        mActivityResumed = false;

        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mAudioPosition = mPlayer.getCurrentPosition();
        }
    }

    protected void onStop() {

        if(mPlayer != null && mPlayer.isPlaying()) {
            Log.e("MusicPlayer", "Stopped");
            mPlayer.stop();
            mPrepared = false;
            mAudioPosition = 0;

        }
    }

    public void onDestroy() {
        super.onDestroy();

        if(mPlayer != null) {
            mPlayer.release();
        }
    }

    protected void onNext() {
        if(songPosition < 3)
            {
                onReset();
                songPosition++;
                onPlay();

            }
    }

    protected void onPrevious() {
        if(songPosition > 0)
        {
            onReset();
            songPosition--;
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

    public String getBand () {


        return band;
    }

    public String getSong () {


        return song;
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
}

    @Override
    public void onCompletion(MediaPlayer mp) {

        onNext();
        getBand();
        getSong();
    }
}

