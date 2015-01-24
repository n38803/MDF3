package android.fullsail.com.mdf3_w3.widgetclasses;

import android.content.Context;
import android.content.Intent;
import android.fullsail.com.mdf3_w3.R;
import android.fullsail.com.mdf3_w3.dataclass.NewsArticle;
import android.fullsail.com.mdf3_w3.fragments.MainListFragment;
import android.util.Log;
import android.widget.RemoteViewsService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CollectionWidgetService extends RemoteViewsService {

    private ArrayList<NewsArticle> mArticles;
    private Context mContext;

    private final String saveFile = "MDF3W3.txt";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        readFile();

        String rTitle = intent.getStringExtra("articleTitle");
        String rAuthor = intent.getStringExtra("articleAuthor");
        String rDate = intent.getStringExtra("articleDate");


        mArticles.add(new NewsArticle(rTitle, rAuthor, rDate));

        writeFile();

        Log.e("WIDGET", "Service Fun");
        return new CollectionWidgetViewFactory(getApplicationContext());
    }

    public void readFile() {
        try {
            FileInputStream fin = mContext.openFileInput(saveFile);
            ObjectInputStream oin = new ObjectInputStream(fin);
            mArticles = (ArrayList<NewsArticle>) oin.readObject();
            oin.close();

        } catch(Exception e) {
            Log.e("sERVICE", "There are no files to pull");


            // static population of data
            mArticles = new ArrayList<NewsArticle>();
            mArticles.add(new NewsArticle("Article One", "John Doe", "01/01/15"));
            mArticles.add(new NewsArticle("Article Two", "Jane Doe", "01/02/15"));
            mArticles.add(new NewsArticle("Article Three", "Julie Doe", "01/04/15"));
            mArticles.add(new NewsArticle("Article Four", "Jason Doe", "01/05/15"));
            mArticles.add(new NewsArticle("Article Five", "Jacob Doe", "01/06/15"));
        }
    }

    private void writeFile() {

        try {
            FileOutputStream fos = mContext.openFileOutput(saveFile, mContext.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mArticles);
            Log.i("SERVICE", "Object Saved Successfully");
            oos.close();

        } catch (Exception e) {
            Log.e("SERVICE", "Save Unsuccessful");
        }


    }


}