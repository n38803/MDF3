package android.fullsail.com.mdf3_w1;

import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Shaun on 1/15/2015.
 */
public class VerticalFragment extends Fragment implements ServiceConnection {

    public static final String TAG = "VERTICAL";
    public static final int STANDARD_NOTIFICATION = 0x01001;
    boolean mBound;

    int sPosition;
    int sLength;
    public int currentTime;

    boolean checkLoop = false;
    MusicService mService;





    public static VerticalFragment newInstance() {
        VerticalFragment frag = new VerticalFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {

        // Create and return view for this fragment.
        View view = _inflater.inflate(R.layout.fragment_vertical, _container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        Intent intent = new Intent(VerticalFragment.this.getActivity(), MusicService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);

    }

    public void grabSongInfo(){

        // Assign view references
        final TextView band = (TextView) getActivity().findViewById(R.id.bandName);
        final TextView song = (TextView) getActivity().findViewById(R.id.songName);
        final TextView time = (TextView) getActivity().findViewById(R.id.songProgress);
        final ImageView art = (ImageView) getActivity().findViewById(R.id.songArt);


        // set current song information to views
        band.setText(mService.getBand());
        song.setText(mService.getSong());

        // TODO - MAKE PHOTO DYNAMIC
        // TODO - adjust size of photo
        art.setImageResource(R.drawable.gotmphoto);

        sLength = mService.getSongLength();
        sPosition = mService.getSongPosition();

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        Log.i("BIND", "CONNECTED");


        // setup binder
        MusicService.LocalBinder binder = (MusicService.LocalBinder)service;
        mService = binder.getService();
        mBound = true;







        // set pending intent
        Intent mainIntent = new Intent(VerticalFragment.this.getActivity(), VerticalFragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(getActivity(), 0, mainIntent, 0);


        // initialize notification & dynamically assign image resources & song info
        final NotificationManager mgr =
                (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext());

        builder.setSmallIcon(R.drawable.ic_stat_av_my_library_music);
        builder.setLargeIcon(BitmapFactory.decodeResource(

        // TODO: MAKE PHOTO DYNAMIC

                getResources(), R.drawable.gotmphoto));
        builder.setContentTitle(mService.getBand());
        builder.setContentText(mService.getSong());
        builder.setContentIntent(pIntent);

        mgr.notify(STANDARD_NOTIFICATION, builder.build());



        // assign loop toggle reference
        final TextView setLoop = (TextView) getActivity().findViewById(R.id.setLoop);

        // Assign button references
        Button play = (Button) getActivity().findViewById(R.id.play);
        Button stop = (Button) getActivity().findViewById(R.id.stop);
        Button pause = (Button) getActivity().findViewById(R.id.pause);
        Button next = (Button) getActivity().findViewById(R.id.next);
        Button previous = (Button) getActivity().findViewById(R.id.previous);
        Button loop = (Button) getActivity().findViewById(R.id.loop);


/*
        while (sPosition <= sLength)
        {

            DateFormat dformat = new SimpleDateFormat("mm:ss");
            String current = dformat.format(sPosition);
            String total = dformat.format(sLength);
            time.setText(current + " / " + total);
            Log.e("TIME", "Timer: " + current);

        }


*/
        // initial establishment of song & info
        grabSongInfo();

        // create seek bar, grab current song length & set sbar max to that.
        final SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.seek);
        currentTime = (sPosition / 100);
        //sBar.setMax(maxTime);

        // listener for seekbar interaction.
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                int newTime = (progress*currentTime);
                // set song to current bar position
                mService.setSongPosition(newTime);

            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // what to do when listener starts tracking bar location

                //sBar.setVerticalScrollbarPosition(mService.getSongPosition());





            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // what to do when listener stops tracking bar location



            }


        });





        // create onClickListeners for each button w/execution of corresponding methods
        loop.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        // check whether or not looping is turned on
                        checkLoop = mService.getLooping();

                        // conditional handler
                        if(checkLoop == true){
                            setLoop.setText("OFF");
                            mService.setLooping(false);
                        }
                        if(checkLoop == false){
                            setLoop.setText("ON");
                            mService.setLooping(true);
                        }

                    }
                }
        );

        play.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPlay();
                        grabSongInfo();
                        mgr.cancel(STANDARD_NOTIFICATION);
                        builder.setContentTitle(mService.getBand());
                        builder.setContentText(mService.getSong());
                        mgr.notify(STANDARD_NOTIFICATION, builder.build());

                    }
                }
        );

        stop.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onStop();
                        grabSongInfo();
                        mgr.cancel(STANDARD_NOTIFICATION);

                    }
                }
        );


        pause.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPause();
                        grabSongInfo();
                        mgr.cancel(STANDARD_NOTIFICATION);

                    }
                }
        );

        next.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onNext();
                        grabSongInfo();
                        mgr.cancel(STANDARD_NOTIFICATION);
                        builder.setContentTitle(mService.getBand());
                        builder.setContentText(mService.getSong());
                        mgr.notify(STANDARD_NOTIFICATION, builder.build());

                    }
                }
        );

        previous.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPrevious();
                        grabSongInfo();
                        mgr.cancel(STANDARD_NOTIFICATION);
                        builder.setContentTitle(mService.getBand());
                        builder.setContentText(mService.getSong());
                        mgr.notify(STANDARD_NOTIFICATION, builder.build());
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
