package android.fullsail.com.mdf3_w1;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class MainActivity extends Activity implements MediaPlayer.OnPreparedListener {

    private static final String SAVE_POSITION = "MainActivity.SAVE_POSITION";

    MediaPlayer mPlayer;
    boolean mActivityResumed;
    boolean mPrepared;
    int mAudioPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // From an activity or other context:
        //Intent intent = new Intent(this, MusicService.class);
        //startService(intent);




        if(savedInstanceState != null && savedInstanceState.containsKey(SAVE_POSITION)) {
            mAudioPosition = savedInstanceState.getInt(SAVE_POSITION, 0);
        }



        Button play = (Button)findViewById(R.id.play);
        Button stop = (Button)findViewById(R.id.stop);
        Button resume = (Button)findViewById(R.id.resume);
        Button pause = (Button)findViewById(R.id.pause);


        play.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        onStart();
                    }
                }
        );

        stop.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        onStop();
                    }
                }
        );

        resume.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        onResume();
                    }
                }
        );

        pause.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        onPause();
                    }
                }
        );







    }

    void onClick() {


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mPlayer == null) {
            // Easy way: mPlayer = MediaPlayer.create(this, R.raw.something_elated);
            // Easy way doesn't require a call to prepare or prepareAsync. Only works for resources.

            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);

            try {
                mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/gotmbaby"));
            } catch(IOException e) {
                e.printStackTrace();

                mPlayer.release();
                mPlayer = null;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mPlayer != null) {
            outState.putInt(SAVE_POSITION, mPlayer.getCurrentPosition());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mActivityResumed = true;
        if(mPlayer != null && !mPrepared) {
            mPlayer.prepareAsync();
        } else if(mPlayer != null && mPrepared) {
            mPlayer.seekTo(mAudioPosition);
            mPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityResumed = false;

        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPrepared = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mPlayer != null) {
            mPlayer.release();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
