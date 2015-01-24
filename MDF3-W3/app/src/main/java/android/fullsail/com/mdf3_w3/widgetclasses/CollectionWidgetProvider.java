package android.fullsail.com.mdf3_w3.widgetclasses;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.fullsail.com.mdf3_w3.AddActivity;
import android.fullsail.com.mdf3_w3.DetailsActivity;
import android.fullsail.com.mdf3_w3.dataclass.NewsArticle;
import android.fullsail.com.mdf3_w3.widgetclasses.CollectionWidgetViewFactory;
import android.fullsail.com.mdf3_w3.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;


public class CollectionWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_VIEW_DETAILS = "android.fullsail.com.mdf3_w3.ACTION_VIEW_DETAILS";
    public static final String ACTION_ADD_ARTICLE = "android.fullsail.com.mdf3_w3.ACTION_ADD_ARTICLE";
    public static final String EXTRA_ITEM = "android.fullsail.com.mdf3_w3.CollectionWidgetProvider.EXTRA_ITEM";
    public final static int ADD_REQUEST = 1;

    RemoteViews rView;

    public final String TAG = "WIDGET PROVIDER";

    private ArrayList<NewsArticle> mArticles;
    private Context mContext;
    private final String saveFile = "MDF3W3.txt";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        //readFile();


        for(int i = 0; i < appWidgetIds.length; i++) {

            Log.e(TAG, "onUpdate() Launched");

            int widgetId = appWidgetIds[i];

            Intent intent = new Intent(context, CollectionWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            rView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rView.setRemoteAdapter(R.id.article_list, intent);
            rView.setRemoteAdapter(R.id.widgetAdd, intent);
            rView.setEmptyView(R.id.article_list, R.id.empty);


            Intent detailIntent = new Intent(ACTION_VIEW_DETAILS);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rView.setPendingIntentTemplate(R.id.article_list, pIntent);

            Intent addIntent = new Intent(ACTION_ADD_ARTICLE);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, addIntent, 0);
            rView.setOnClickPendingIntent(R.id.widgetAdd, pendingIntent );

            AppWidgetManager.getInstance(context).updateAppWidget(widgetId, rView);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }




    @Override
    public void onReceive(Context context, Intent intent) {



        if(intent.getAction().equals(ACTION_VIEW_DETAILS)) {
            NewsArticle article = (NewsArticle)intent.getSerializableExtra(EXTRA_ITEM);
            if(article != null) {
                Intent details = new Intent(context, DetailsActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(DetailsActivity.EXTRA_ITEM, article);
                context.startActivity(details);

                Log.i(TAG, "Pending Intent launched from onReceive(): DETAIL ACTIVITY");
            }
        }

        else if(intent.getAction().equals(ACTION_ADD_ARTICLE)) {

            Intent publish = new Intent(context, AddActivity.class);
            publish.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(publish);

            Log.i(TAG, "Pending Intent launched from onReceive(): ADD ACTIVITY // ");




        }






        super.onReceive(context, intent);
       // super.onUpdate(mContext, AppWidgetManager.getInstance(mContext), );
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


}