package android.fullsail.com.mdf3_w1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by Shaun on 1/11/2015.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener {

    Context context;
    // URI grabs
    String way1 = "android.resource://" + getPackageName() + "/raw/gotmlettinggo";
    String way2 = "android.resource://" + getPackageName() + "/" + R.raw.gotmlettinggo;

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

    /*
    // FROM FS LESSON

    // Player created, in idle state.
    MediaPlayer player = new MediaPlayer();

// Player initialized, in initialized state.
    player.setDataSource(this, Uri.parse("android.resource://..."));
// Setting a prepared listener.
    player.setOnPreparedListener(new OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            // Player prepared!
            mp.start();
        }
    });
    */



    // FROM ANDROID DEVELOPERS

    private static final String ACTION_PLAY = "android.fullsail.com.mdf3_w1.action.PLAY";
    MediaPlayer mMediaPlayer = null;

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(ACTION_PLAY)) {
            try {
                mMediaPlayer.setDataSource(this, Uri.parse(way1));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        }

        // added manually to return 0
        return 0;
    }

    // Called when MediaPlayer is ready
    public void onPrepared(MediaPlayer player) {
        player.start();
    }



    @Override
    public IBinder onBind(Intent intent) {
        // Don't allow binding.
        return null;
    }

}