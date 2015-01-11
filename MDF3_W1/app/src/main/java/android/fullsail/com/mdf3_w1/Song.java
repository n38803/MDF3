package android.fullsail.com.mdf3_w1;

/**
 * Created by Shaun on 1/11/2015.
 */
public class Song {

    private String song;
    private String band;
    private String track;

    public Song (String songBand, String songName, String songTrack) {
        song=songName;
        band=songBand;
        track=songTrack;

    }

    public String getSong(){return song;}
    public String getArtist(){return band;}
    public String getTrack(){return track;}

}
