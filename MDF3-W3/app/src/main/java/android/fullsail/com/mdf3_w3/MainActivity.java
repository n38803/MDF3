package android.fullsail.com.mdf3_w3;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.fullsail.com.mdf3_w3.dataclass.NewsArticle;
import android.fullsail.com.mdf3_w3.fragments.MainListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;




public class MainActivity extends Activity implements MainListFragment.ArticleListener {

    private final String TAG = "MAIN ACTIVITY";
    private final String saveFile = "MDF3W3.txt";


    public static final int ADDREQUEST = 2;

    private ArrayList<NewsArticle> mArticleList;
    public Button addButton;

    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check to see whether or not there is a saved instance of the fragment
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainListFragment())
                    .commit();
        }



        readFile();

        addButton = (Button) findViewById(R.id.addButton);





    }


    public void onClick(View v){

        Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
        //addIntent.putExtra("contactName", mContactDataList.get());
        startActivityForResult(addIntent, ADDREQUEST);
        //startActivity(addIntent);

        Log.i(TAG, "Button Clicked");


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

    protected void onActivityResult(int requestCode, int resultCode, Intent data){


        if(requestCode == ADDREQUEST && resultCode == RESULT_OK){
            String rTitle = data.getStringExtra("articleTitle");
            String rAuthor = data.getStringExtra("articleAuthor");
            String rDate = data.getStringExtra("articleDate");
            String action = data.getStringExtra("action");

            mArticleList.add(new NewsArticle(rTitle, rAuthor, rDate));


            MainListFragment nf = (MainListFragment) getFragmentManager().findFragmentById(R.id.container);
            nf.updateListData();

            writeFile();

            if (action.equals("add")){
                Toast.makeText(this, rTitle + " added to Article List.", Toast.LENGTH_LONG).show();


            }
        }
    }


    //INTERFACE METHODS

    @Override
    public void viewArticle(int position){

        // Declare Intent
        Intent detailIntent = new Intent(this, DetailsActivity.class);

        // pass position from list into intent by using constant from detail activity
        detailIntent.putExtra(DetailsActivity.EXTRA_ITEM, mArticleList.get(position));

        // start detail activity by passing intent we wish to load
        startActivity(detailIntent);
    }



    @Override
    public ArrayList<NewsArticle> getArticles() {
        return mArticleList;
    }

    // Creates local storage file
    private void writeFile() {

        try {
            FileOutputStream fos = openFileOutput(saveFile, this.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mArticleList);
            Log.i(TAG, "Object Saved Successfully");
            oos.close();

        } catch (Exception e) {
            Log.e(TAG, "Save Unsuccessful");
        }


    }

    private void readFile() {


        try {
            FileInputStream fin = openFileInput(saveFile);
            ObjectInputStream oin = new ObjectInputStream(fin);
            mArticleList = (ArrayList<NewsArticle>) oin.readObject();
            oin.close();

        } catch(Exception e) {
            Log.e(TAG, "There are no files to pull");

            Toast.makeText(this, "No data Saved - Static information Populated.", Toast.LENGTH_LONG).show();

            // static population of data
            mArticleList = new ArrayList<NewsArticle>();
            mArticleList.add(new NewsArticle("Article One", "John Doe", "01/01/15"));
            mArticleList.add(new NewsArticle("Article Two", "Jane Doe", "01/02/15"));
            mArticleList.add(new NewsArticle("Article Three", "Julie Doe", "01/04/15"));
            mArticleList.add(new NewsArticle("Article Four", "Jason Doe", "01/05/15"));
            mArticleList.add(new NewsArticle("Article Five", "Jacob Doe", "01/06/15"));
        }
    }

}
