package android.fullsail.com.mdf3_w3.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.fullsail.com.mdf3_w3.R;
import android.fullsail.com.mdf3_w3.dataclass.NewsArticle;
import android.fullsail.com.mdf3_w3.dataclass.NewsAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainListFragment extends Fragment {

    private final String TAG = "MAIN LIST FRAGMENT";

    private ArticleListener mListener;
    private ArrayList<NewsArticle> mArticleList;

    public interface ArticleListener{
        public void viewArticle(int position);
        public ArrayList<NewsArticle> getArticles();
    }

    public MainListFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof ArticleListener) {
            mListener = (ArticleListener) activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement DetailListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListView articleListView = (ListView) getView().findViewById(R.id.articleList);
        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), mListener.getArticles());
        articleListView.setAdapter(newsAdapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.viewArticle(position);
            }
        });


    }

    public void updateListData(){
        ListView contactList = (ListView) getView().findViewById(R.id.articleList);
        BaseAdapter contactAdapter = (BaseAdapter) contactList.getAdapter();
        contactAdapter.notifyDataSetChanged();
    }



}