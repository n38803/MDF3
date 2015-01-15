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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration launchConfig = new Configuration();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new VerticalFragment())
                    .commit();
            Log.i(TAG, "Application launched in Vertical Orientation");
        }
        /*
        else if (savedInstanceState == null && launchConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new HorizontalFragment())
                    .commit();
            Log.i(TAG, "Application launched in Horizontal Orientation");
        }
        else if (savedInstanceState != null && launchConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.fragment_vertical);
        }
        else if (savedInstanceState != null && launchConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.fragment_horizontal);
        }
        */
        else
        {
            Log.e(TAG, "ERROR");
        }






    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // DEVICE IS IN HORIZONTAL
            setContentView(R.layout.fragment_horizontal);
            Log.i(TAG, "Orientation Change Horizontal");

        }

        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            // DEVICE IS IN VERTICAL
            setContentView(R.layout.fragment_vertical);
            Log.i(TAG, "Orientation Change Vertical");


        }
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
