package android.fullsail.com.mdf3_w3.widgetclasses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.fullsail.com.mdf3_w3.AddActivity;
import android.fullsail.com.mdf3_w3.dataclass.NewsArticle;
import android.fullsail.com.mdf3_w3.R;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class CollectionWidgetViewFactory implements RemoteViewsFactory {

    private static final int ID_CONSTANT = 0x0101010;
    private final String TAG = "WIDGET VIEW";
    private final String saveFile = "MDF3W3.txt";

    private ArrayList<NewsArticle> mArticles;
    private Context mContext;


    public CollectionWidgetViewFactory(Context context) {
        mContext = context;
        mArticles = new ArrayList<NewsArticle>();
    }

    @Override
    public void onCreate() {

        Log.e(TAG, "Widget onCreate");
        readFile();


    }



    public void readFile() {
        try {
            FileInputStream fin = mContext.openFileInput(saveFile);
            ObjectInputStream oin = new ObjectInputStream(fin);
            mArticles = (ArrayList<NewsArticle>) oin.readObject();
            oin.close();

        } catch(Exception e) {
            Log.e(TAG, "There are no files to pull");


            // static population of data
            mArticles = new ArrayList<NewsArticle>();
            mArticles.add(new NewsArticle("Article One", "John Doe", "01/01/15"));
            mArticles.add(new NewsArticle("Article Two", "Jane Doe", "01/02/15"));
            mArticles.add(new NewsArticle("Article Three", "Julie Doe", "01/04/15"));
            mArticles.add(new NewsArticle("Article Four", "Jason Doe", "01/05/15"));
            mArticles.add(new NewsArticle("Article Five", "Jacob Doe", "01/06/15"));
        }
    }

    @Override
    public int getCount() {
        return mArticles.size();
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }



    @Override
    public RemoteViews getViewAt(int position) {

        Log.e(TAG, "RemoteViews getViewAt");

        NewsArticle article = mArticles.get(position);

        RemoteViews itemView = new RemoteViews(mContext.getPackageName(), R.layout.article_item);

        itemView.setTextViewText(R.id.title, article.getTitle());
        itemView.setTextViewText(R.id.author, article.getAuthor());
        itemView.setTextViewText(R.id.date, article.getDate());


        Intent intent = new Intent();
        intent.putExtra(CollectionWidgetProvider.EXTRA_ITEM, article);
        itemView.setOnClickFillInIntent(R.id.article_item, intent);

        return itemView;
    }



    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        // Heavy lifting code can go here without blocking the UI.
        // You would update the data in your collection here as well.
        Log.w(TAG, "Data has changed!");
        readFile();
    }



    private void writeFile() {

        try {
            FileOutputStream fos = mContext.openFileOutput(saveFile, mContext.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mArticles);
            Log.i(TAG, "Object Saved Successfully");
            oos.close();

        } catch (Exception e) {
            Log.e(TAG, "Save Unsuccessful");
        }


    }

    @Override
    public void onDestroy() {
        //unregisterReceiver(mAppReceiver);
        mArticles.clear();
    }

}