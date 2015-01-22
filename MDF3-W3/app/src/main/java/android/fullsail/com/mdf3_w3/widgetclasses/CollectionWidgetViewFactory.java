package android.fullsail.com.mdf3_w3.widgetclasses;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.fullsail.com.mdf3_w3.dataclass.NewsArticle;
import android.fullsail.com.mdf3_w3.R;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class CollectionWidgetViewFactory implements RemoteViewsFactory {

    private static final int ID_CONSTANT = 0x0101010;

    private ArrayList<NewsArticle> mArticles;
    private Context mContext;

    public CollectionWidgetViewFactory(Context context) {
        mContext = context;
        mArticles = new ArrayList<NewsArticle>();
    }

    @Override
    public void onCreate() {
        String[] titles = { "New Phones Released!", "Random App Makes $300 Million",
                "Google Glass Robots", "Arduino Used in Mobile", "Orioles Win World Series" };
        String[] authors = { "Phone Scoop", "Yahoo Marketing Department", "Cyborg Alliance",
                "Some Open Source Person", "Roch Kubatko" };
        String[] dates = { "Everyday", "June 20, 2012", "September 10, 2014", "August 9, 2014", "November 10, 2014" };

        for(int i = 0; i < 5; i++) {
            mArticles.add(new NewsArticle(titles[i], authors[i], dates[i]));
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
    }

    @Override
    public void onDestroy() {
        mArticles.clear();
    }

}