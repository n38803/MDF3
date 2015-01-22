package android.fullsail.com.mdf3_w3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends Activity {

    public static final String EXTRA_ITEM = "com.fullsail.android.DetailsActivity.EXTRA_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        NewsArticle article = (NewsArticle)intent.getSerializableExtra(EXTRA_ITEM);
        if(article == null) {
            finish();
            return;
        }

        TextView tv = (TextView)findViewById(R.id.title);
        tv.setText(article.getTitle());

        tv = (TextView)findViewById(R.id.author);
        tv.setText(article.getAuthor());

        tv = (TextView)findViewById(R.id.date);
        tv.setText(article.getDate());
    }
}
