package android.fullsail.com.mdf3_w1;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity implements MediaPlayer.OnPreparedListener {

    private static final String SAVE_POSITION = "MainActivity.SAVE_POSITION";

    MediaPlayer mPlayer;
    boolean mActivityResumed;
    boolean mPrepared;
    int mAudioPosition;
    int songPosition;
    boolean mIdle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // From an activity or other context:
        //Intent intent = new Intent(this, MusicService.class);
        //startService(intent);



        mPrepared = mActivityResumed = false;
        mAudioPosition = 0;
        songPosition = 0;
        mIdle = true;


        if(savedInstanceState != null && savedInstanceState.containsKey(SAVE_POSITION)) {
            mAudioPosition = savedInstanceState.getInt(SAVE_POSITION, 0);
        }


        // Assign button references
        Button play = (Button)findViewById(R.id.play);
        Button stop = (Button)findViewById(R.id.stop);
        Button pause = (Button)findViewById(R.id.pause);
        Button next = (Button)findViewById(R.id.next);
        Button previous = (Button)findViewById(R.id.previous);


        // create onClickListeners for each button w/execution of corresponding methods
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


        pause.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        onPause();
                    }
                }
        );

        next.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if(songPosition < 3)
                        {
                            onReset();
                            songPosition++;
                            onStart();

                        }



                    }
                }
        );

        previous.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if(songPosition > 0)
                        {
                            onReset();
                            songPosition--;
                            onStart();
                        }

                    }
                }
        );










    }


    @Override
    protected void onStart() {
        super.onStart();

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

        // Assign textviews
        TextView band = (TextView)findViewById(R.id.bandName);
        TextView song = (TextView)findViewById(R.id.songName);


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
            Log.i("Preparing", "Track: " + songPosition);
            mPlayer.prepareAsync();
        } else if(mPlayer != null && mPrepared) {
            Log.i("Resuming", "Track: " + songPosition);
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
            mAudioPosition = mPlayer.getCurrentPosition();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mPlayer != null && mPlayer.isPlaying()) {
            Log.e("MusicPlayer", "Stopped");
            mPlayer.stop();
            mPrepared = false;
            mAudioPosition = 0;

        }
    }

    @Override
    protected void onDestroy() {
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
