package android.fullsail.com.mdf3_w1;

import android.media.Image;

/**
 * Created by Shaun on 1/11/2015.
 */
public class Song {

    private String song;
    private String band;
    private String track;
    private int art;

    public Song (String songBand, String songName, String songTrack, int songArt) {
        song=songName;
        band=songBand;
        track=songTrack;
        art=songArt;

    }

    public String getSong(){return song;}
    public String getArtist(){return band;}
    public String getTrack(){return track;}
   public int getArt(){
       return art;
   }

}
