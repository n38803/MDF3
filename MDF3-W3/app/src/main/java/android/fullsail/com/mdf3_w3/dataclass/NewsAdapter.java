package android.fullsail.com.mdf3_w3.dataclass;

import android.content.Context;
import android.fullsail.com.mdf3_w3.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {

    private static final long ID_CONSTANT = 0x01000000;

    Context mContext;
    ArrayList<NewsArticle> mArticles;

    public NewsAdapter(Context context, ArrayList<NewsArticle> contacts) {
        mContext = context;
        mArticles = contacts;
    }

    @Override
    public int getCount() {
        return mArticles.size();
    }

    @Override
    public NewsArticle getItem(int position) {
        return mArticles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.article_item, parent, false);
        }

        NewsArticle article = getItem(position);
        TextView articleTitle = (TextView) convertView.findViewById(R.id.title);
        articleTitle.setText(article.getTitle());

        return convertView;
    }
}