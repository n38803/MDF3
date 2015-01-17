package android.fullsail.com.mdf3_w1;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    String TAG = "MAIN_ACTIVITY";
    public boolean portraitMode;

    public void getOrientation(){
        Configuration config = new Configuration();

        // DEVICE IS IN HORIZONTAL
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            portraitMode = false;
            Log.i(TAG, "Orientation is Horizontal");

        }
        // DEVICE IS IN HORIZONTAL
        else if (config.orientation == Configuration.ORIENTATION_PORTRAIT){
            portraitMode = true;
            Log.i(TAG, "Orientation Change Vertical");


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration config = new Configuration();

        // DEVICE IS IN HORIZONTAL
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            portraitMode = false;
            Log.i(TAG, "Orientation is Horizontal");

        }
        // DEVICE IS IN HORIZONTAL
        else if (config.orientation == Configuration.ORIENTATION_PORTRAIT){
            portraitMode = true;
            Log.i(TAG, "Orientation Change Vertical");


        }
        getOrientation();


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new VerticalFragment())
                    .commit();
           // Log.i(TAG, "Application launched in Vertical Orientation = ");
        }
       /* else if (savedInstanceState == null && portraitMode == false) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new HorizontalFragment())
                    .commit();
            Log.i(TAG, "Application launched in Horizontal Orientation = ");
        }
        */






    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        getOrientation();

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
