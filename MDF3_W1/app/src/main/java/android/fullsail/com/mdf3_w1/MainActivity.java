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

import android.fullsail.com.mdf3_w1.MusicService.BoundServiceBinder;


public class MainActivity extends Activity implements ServiceConnection {

    public static final int STANDARD_NOTIFICATION = 0x01001;

    boolean mBound;
    MusicService mService;
    private MusicService mBoundService;
    Context context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, this, Context.BIND_AUTO_CREATE);





        // Assign textviews
        TextView band = (TextView) findViewById(R.id.bandName);
        TextView song = (TextView) findViewById(R.id.songName);









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

        NotificationManager mgr =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(
                getResources(), R.drawable.ic_launcher));
        builder.setContentTitle("Standard Title");
        builder.setContentText("Standard Message");
        mgr.notify(STANDARD_NOTIFICATION, builder.build());

        BoundServiceBinder binder = (BoundServiceBinder)service;
        final MusicService myService = binder.getService();

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
                        myService.onPlay();
                    }
                }
        );

        stop.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        myService.onStop();
                    }
                }
        );


        pause.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        myService.onPause();
                    }
                }
        );

        next.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                       myService.onNext();


                    }
                }
        );

        previous.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                       myService.onPrevious();
                    }
                }
        );



    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
