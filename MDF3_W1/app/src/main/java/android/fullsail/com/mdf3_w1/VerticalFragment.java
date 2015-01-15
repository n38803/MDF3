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

/**
 * Created by Shaun on 1/15/2015.
 */
public class VerticalFragment extends Fragment implements ServiceConnection {

    public static final String TAG = "VERTICAL";
    public static final int STANDARD_NOTIFICATION = 0x01001;
    boolean mBound;

    int sPosition;
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        Log.i("BIND", "CONNECTED");


        // setup binder
        MusicService.LocalBinder binder = (MusicService.LocalBinder)service;
        mService = binder.getService();
        mBound = true;

        // Assign textviews
        final TextView band = (TextView) getActivity().findViewById(R.id.bandName);
        final TextView song = (TextView) getActivity().findViewById(R.id.songName);
        final ImageView art = (ImageView) getActivity().findViewById(R.id.songArt);

        // assign current song information to views
        band.setText(mService.getBand());
        song.setText(mService.getSong());

        // TODO - MAKE PHOTO DYNAMIC
        // TODO - adjust size of photo
        art.setImageResource(R.drawable.gotmphoto);


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






        // Assign button references
        Button play = (Button) getActivity().findViewById(R.id.play);
        Button stop = (Button) getActivity().findViewById(R.id.stop);
        Button pause = (Button) getActivity().findViewById(R.id.pause);
        Button next = (Button) getActivity().findViewById(R.id.next);
        Button previous = (Button) getActivity().findViewById(R.id.previous);





        // create seek bar, grab current song length & set sbar max to that.
        final SeekBar sBar = (SeekBar) getActivity().findViewById(R.id.seek);
        //int maxTime = mService.getSongLength();
        //sBar.setMax(maxTime);

        // listener for seekbar interaction.
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                // set song to current bar position
                //mService.setSongPosition(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // what to do when listener starts tracking bar location

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // what to do when listener stops tracking bar location



            }


        });





        // create onClickListeners for each button w/execution of corresponding methods
        play.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPlay();
                        band.setText(mService.getBand());
                        song.setText(mService.getSong());
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
                        band.setText(mService.getBand());
                        song.setText(mService.getSong());
                        mgr.cancel(STANDARD_NOTIFICATION);

                    }
                }
        );


        pause.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onPause();
                        band.setText(mService.getBand());
                        song.setText(mService.getSong());
                        mgr.cancel(STANDARD_NOTIFICATION);

                    }
                }
        );

        next.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        mService.onNext();
                        band.setText(mService.getBand());
                        song.setText(mService.getSong());
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
                        band.setText(mService.getBand());
                        song.setText(mService.getSong());
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
