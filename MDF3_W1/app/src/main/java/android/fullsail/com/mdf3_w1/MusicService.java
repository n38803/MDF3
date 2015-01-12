package android.fullsail.com.mdf3_w1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
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
public class MusicService extends Service implements MediaPlayer.OnPreparedListener {

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


    @Override
    public void onCreate() {
        super.onCreate();






    }

    @Override
    public IBinder onBind(Intent intent) {
        // Don't allow binding.
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            Log.e("SERVICES", "Started");
            onPlay();



        return Service.START_STICKY;
    }

    protected void onPlay() {


        if(mPlayer == null) {
            // assign & initiate media player
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);
            songPosition = 0;
            mIdle = false;

            try {
                mPlayer.setDataSource(this, Uri.parse(songs[songPosition].getTrack()));
                mIdle = false;
                Log.i("Initiating", "Track: " + songPosition);
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




        band.setText(songs[songPosition].getArtist());
        song.setText(songs[songPosition].getSong());
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

    protected void onReset() {
        if(mPlayer != null) {

            Log.e("MusicPlayer", "RESET");
            mAudioPosition = 0;
            mPlayer.reset();
            mPrepared = false;
            mIdle = true;


        }
    }



    @Override
    public void onPrepared(MediaPlayer mp) {
        mPrepared = true;

        if(mPlayer != null && mActivityResumed) {

            mPlayer.seekTo(mAudioPosition);
            mPlayer.start();
        }
    }
}