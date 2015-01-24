package android.fullsail.com.mdf3_w3;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.fullsail.com.mdf3_w3.dataclass.NewsArticle;
import android.fullsail.com.mdf3_w3.fragments.AddFragment;
import android.fullsail.com.mdf3_w3.widgetclasses.CollectionWidgetProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AddActivity extends Activity  {

    public static final String ACTION_ADD_ARTICLE = "android.fullsail.com.mdf3_w3.ACTION_ADD_ARTICLE";

    public TextView inputTitle;
    public TextView inputAuthor;
    public TextView inputDate;

    private final String saveFile = "MDF3W3.txt";
    private final String TAG = "ADD ACTIVITY";

    CollectionWidgetProvider mAppReceiver = new CollectionWidgetProvider();

    private ArrayList<NewsArticle> mArticleList;

    public String aTitle;
    public String aAuthor;
    public String aDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Check to see whether or not there is a saved instance of the fragment
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new AddFragment())
                    .commit();
        }





    }

    public void onCancel(View v){
        // Grabs intent from main activity
        Intent addIntent = getIntent();

        clearDisplay();
        finish();


    }




    public void onSave(View v){

        // Grabs intent from main activity
        Intent addIntent = getIntent();


        inputTitle = (TextView) findViewById(R.id.inputTitle);
        inputAuthor = (TextView) findViewById(R.id.inputAuthor);
        inputDate = (TextView) findViewById(R.id.inputDate);

        // assign input to variables
        aTitle = inputTitle.getText().toString();
        aAuthor = inputAuthor.getText().toString();
        aDate = inputDate.getText().toString();

        //registerReceiver(mAppReceiver, new IntentFilter(ACTION_ADD_ARTICLE));

        Intent broadcast  = new Intent(ACTION_ADD_ARTICLE);
        broadcast .putExtra("articleTitle", aTitle);
        broadcast .putExtra("articleAuthor", aAuthor);
        broadcast .putExtra("articleDate", aDate);
        broadcast .putExtra("action", "add");
        sendBroadcast(broadcast);

        Intent aIntent = new Intent();
        aIntent.putExtra("articleTitle", aTitle);
        aIntent.putExtra("articleAuthor", aAuthor);
        aIntent.putExtra("articleDate", aDate);
        aIntent.putExtra("action", "add");
        setResult(RESULT_OK, aIntent);


        clearDisplay();
        finish();



    }

    private void clearDisplay(){
        inputTitle = (TextView) findViewById(R.id.inputTitle);
        inputAuthor = (TextView) findViewById(R.id.inputAuthor);
        inputDate = (TextView) findViewById(R.id.inputDate);

        inputTitle.setText("");
        inputAuthor.setText("");
        inputDate.setText("");
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
    public void onDestroy() {
        super.onDestroy();

    }

}