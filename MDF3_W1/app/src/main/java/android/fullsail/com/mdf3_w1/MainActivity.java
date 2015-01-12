package android.fullsail.com.mdf3_w1;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NotificationCompat;


import java.io.IOException;
import java.util.ArrayList;

import android.fullsail.com.mdf3_w1.MusicService.LocalBinder;


public class MainActivity extends Activity implements ServiceConnection {

    public static final int STANDARD_NOTIFICATION = 0x01001;

    boolean mBound;
    MusicService mService;

    public String bandName;
    public String songName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
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



    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        Log.i("BIND", "CONNECTED");

        LocalBinder binder = (LocalBinder )service;
        mService = binder.getService();
        mBound = true;

        // Assign textviews
        final TextView band = (TextView) findViewById(R.id.bandName);
        final TextView song = (TextView) findViewById(R.id.songName);

        bandName = mService.getBand();
        songName = mService.getSong();

        band.setText(bandName);
        song.setText(songName);





        NotificationManager mgr =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_launcher));
        builder.setContentTitle("NOW PLAYING: ");
        builder.setContentText(bandName + " - " + songName);
        mgr.notify(STANDARD_NOTIFICATION, builder.build());



        // Assign button references
        Button play = (Button) findViewById(R.id.play);
        Button stop = (Button) findViewById(R.id.stop);
        Button pause = (Button) findViewById(R.id.pause);
        Button next = (Button) findViewById(R.id.next);
        Button previous = (Button) findViewById(R.id.previous);




        // create onClickListeners for each button w/execution of corresponding methods
        play.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPlay();
                        band.setText(bandName);
                        song.setText(songName);
                    }
                }
        );

        stop.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onStop();
                        band.setText(bandName);
                        song.setText(songName);
                    }
                }
        );


        pause.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPause();
                        band.setText(bandName);
                        song.setText(songName);
                    }
                }
        );

        next.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onNext();
                        band.setText(bandName);
                        song.setText(songName);


                    }
                }
        );

        previous.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPrevious();
                        band.setText(bandName);
                        song.setText(songName);
                    }
                }
        );



    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        mService = null;
        mBound = false;

    }
}
