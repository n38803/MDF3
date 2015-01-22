package android.fullsail.com.mdf3_w3.dataclass;

import java.io.Serializable;

public class NewsArticle implements Serializable {

    private static final long serialVersionUID = 517116325584636891L;

    private String mTitle;
    private String mAuthor;
    private String mDate;

    public NewsArticle(String _title, String _author, String _date) {
        mTitle = _title;
        mAuthor = _author;
        mDate = _date;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }
}