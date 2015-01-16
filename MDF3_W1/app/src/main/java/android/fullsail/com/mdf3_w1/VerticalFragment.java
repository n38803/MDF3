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
import android.os.Handler;

/**
 * Created by Shaun on 1/15/2015.
 */
public class VerticalFragment extends Fragment implements ServiceConnection {

    public static final String TAG = "VERTICAL";
    public static final int STANDARD_NOTIFICATION = 0x01001;
    boolean mBound;

    int sPosition;
    int sLength;
    int sCurrent;

    boolean checkLoop = false;
    MusicService mService;

    private Handler progressHandler = new Handler();


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

    public void setSongInfo(){

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



    }

    public void getSongMetrics(){
        sLength = mService.getSongLength();
        sPosition = mService.getSongPosition();
    }

    public void resetProgress(){

        SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.seek);
        sBar.setProgress(0);

    }


    private Runnable updateTime = new Runnable() {

        @Override
        public void run() {

            SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.seek);
            TextView time = (TextView) getActivity().findViewById(R.id.songProgress);

            if(mService.isNotNull())
            {
                DateFormat dformat = new SimpleDateFormat("mm:ss:SS");
                String current = dformat.format(mService.getSongPosition());
                time.setText(current);


                sCurrent = mService.getSongPosition() / 1000;
                sBar.setProgress(sCurrent);
                progressHandler.postDelayed(this, 1000);
            }
            else if (!mService.isNotNull())
            {
                sBar.setProgress(0);
            }

        }
    };

    private void updateProgress(){
        progressHandler.postDelayed(updateTime, 100);
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
        SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.seek);


        // initial establishment of song & info
        setSongInfo();

        // start progress bar
        updateProgress();

        //sBar.setMax(maxTime);

        // seekbar listener for user interaction
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // set song position to coincide with seekbar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                if(mService.isNotNull() && fromUser)
                {
                    getSongMetrics();
                    int croppedLength = (sLength / 100);
                    int newTime = (progress*croppedLength);
                    Log.e(TAG, "Progress: " + progress + " / " + newTime);
                    // set song to current bar position
                    mService.setSongPosition(newTime);

                }

            }


            // what to do when listener starts tracking bar location
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                //updateProgress();
                //sBar.setVerticalScrollbarPosition(mService.getSongPosition());

            }

            // what to do when listener stops tracking bar location
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
                        setSongInfo();
                        //updateProgress();
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
                        resetProgress();
                        setSongInfo();
                        mgr.cancel(STANDARD_NOTIFICATION);

                    }
                }
        );


        pause.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPause();
                        setSongInfo();
                        mgr.cancel(STANDARD_NOTIFICATION);

                    }
                }
        );

        next.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onNext();
                        setSongInfo();
                        //resetProgress();
                        //updateProgress();
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
                        setSongInfo();
                        //resetProgress();
                        //updateProgress();
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
