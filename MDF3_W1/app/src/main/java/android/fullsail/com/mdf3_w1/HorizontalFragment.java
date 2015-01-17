package android.fullsail.com.mdf3_w1;

import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
public class HorizontalFragment extends Fragment implements ServiceConnection {

    public static final String TAG = "HORIZONTAL";
    public static final int STANDARD_NOTIFICATION = 0x01002;
    boolean mBound;

    int sPosition;
    int sLength;
    int sCurrent;

    boolean checkLoop = false;
    MusicService mService;

    private Handler progressHandler = new Handler();


    public static HorizontalFragment newInstance() {
        HorizontalFragment frag = new HorizontalFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {

        // Create and return view for this fragment.
        View view = _inflater.inflate(R.layout.fragment_horizontal, _container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);


        Intent intent = new Intent(HorizontalFragment.this.getActivity(), MusicService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, this, Context.BIND_AUTO_CREATE);


    }

    public void setSongInfo(){

        // Assign view references
        final TextView band = (TextView) getActivity().findViewById(R.id.hbandName);
        final TextView song = (TextView) getActivity().findViewById(R.id.hsongName);
        final ImageView art = (ImageView) getActivity().findViewById(R.id.hSongArt);


        // set current song information to views
        band.setText(mService.getBand());
        song.setText(mService.getSong());
        art.setImageResource(mService.getArt());



    }

    public void getSongMetrics(){
        sLength = mService.getSongLength();
        sPosition = mService.getSongPosition();
    }

    public void resetProgress(){

        SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.hseek);
        sBar.setProgress(0);
        sLength = 0;
        sPosition = 0;
        sCurrent = 0;

    }


    private Runnable updateTime = new Runnable() {

        @Override
        public void run() {

            SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.hseek);
            TextView time = (TextView) getActivity().findViewById(R.id.hsongProgress);

            if(mService.isNotNull())
            {
                DateFormat dformat = new SimpleDateFormat("mm:ss");
                String current = dformat.format(mService.getSongPosition());
                time.setText("[ " + current + " ] ");


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
        Intent mainIntent = new Intent(HorizontalFragment.this.getActivity(), HorizontalFragment.class);
        PendingIntent pIntent = PendingIntent.getActivity(getActivity(), 0, mainIntent, 0);

        /*
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
        */


        // assign loop toggle reference
        final TextView setLoop = (TextView) getActivity().findViewById(R.id.hsetLoop);

        // Assign button references
        Button play = (Button) getActivity().findViewById(R.id.hplay);
        Button stop = (Button) getActivity().findViewById(R.id.hstop);
        Button pause = (Button) getActivity().findViewById(R.id.hpause);
        Button next = (Button) getActivity().findViewById(R.id.hnext);
        Button previous = (Button) getActivity().findViewById(R.id.hprevious);
        Button loop = (Button) getActivity().findViewById(R.id.hloop);
        SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.hseek);


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
                        //mgr.cancel(STANDARD_NOTIFICATION);
                        //builder.setContentTitle(mService.getBand());
                        //builder.setContentText(mService.getSong());
                        //mgr.notify(STANDARD_NOTIFICATION, builder.build());

                    }
                }
        );

        stop.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        resetProgress();
                        mService.onStop();

                        setSongInfo();
                        // mgr.cancel(STANDARD_NOTIFICATION);

                    }
                }
        );


        pause.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPause();
                        setSongInfo();
                        // mgr.cancel(STANDARD_NOTIFICATION);

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
                        // mgr.cancel(STANDARD_NOTIFICATION);
                        // builder.setContentTitle(mService.getBand());
                        // builder.setContentText(mService.getSong());
                        // mgr.notify(STANDARD_NOTIFICATION, builder.build());

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
                        // mgr.cancel(STANDARD_NOTIFICATION);
                        //builder.setContentTitle(mService.getBand());
                        // builder.setContentText(mService.getSong());
                        // mgr.notify(STANDARD_NOTIFICATION, builder.build());
                    }
                }
        );



    }



    @Override
    public void onServiceDisconnected(ComponentName name) {

        // mService = null;
        //mBound = false;

        if(mBound){
            // mService.unbindService();
            mBound = false;
            mService = null;
        }

    }

}
