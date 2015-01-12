package android.fullsail.com.mdf3_w1;

import android.app.Activity;
import android.content.Intent;
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


public class MainActivity extends Activity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, MusicService.class);




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
        TextView band = (TextView) findViewById(R.id.bandName);
        TextView song = (TextView) findViewById(R.id.songName);


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
                        startService(intent);
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
                            onPlay();

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
                            onPlay();
                        }

                    }
                }
        );






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
