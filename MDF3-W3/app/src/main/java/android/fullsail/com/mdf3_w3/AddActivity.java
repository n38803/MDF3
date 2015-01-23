package android.fullsail.com.mdf3_w3;

import android.app.Activity;
import android.content.Intent;
import android.fullsail.com.mdf3_w3.dataclass.NewsArticle;
import android.fullsail.com.mdf3_w3.fragments.AddFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class AddActivity extends Activity  {

    //public static final String ADDEXTRA = "android.fullsail.com.mdf3_w3.DetailsActivity.ADDEXTRA";

    public TextView inputTitle;
    public TextView inputAuthor;
    public TextView inputDate;

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

        // Grabs intent from main activity
        Intent addIntent = getIntent();

        // verify there is an intent available to grab
        if(addIntent != null){

            // do stuff after grabbing intent.
        }




    }

    public void onCancel(View v){
        clearDisplay();
        finish();


    }

    public void onSave(View v){
        inputTitle = (TextView) findViewById(R.id.inputTitle);
        inputAuthor = (TextView) findViewById(R.id.inputAuthor);
        inputDate = (TextView) findViewById(R.id.inputDate);

        // assign input to variables
        aTitle = inputTitle.getText().toString();
        aAuthor = inputAuthor.getText().toString();
        aDate = inputDate.getText().toString();


        Intent intent = new Intent();
        intent.putExtra("articleTitle", aTitle);
        intent.putExtra("articleAuthor", aAuthor);
        intent.putExtra("articleDate", aDate);
        intent.putExtra("action", "add");
        setResult(RESULT_OK, intent);

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


}