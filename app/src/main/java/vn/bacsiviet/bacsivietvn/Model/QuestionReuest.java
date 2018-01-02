package vn.bacsiviet.bacsivietvn.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 14/12/2017.
 */

public class QuestionReuest implements Serializable {
    @SerializedName("page")
    private String keySearch;
    @SerializedName("current_page")
    private int pageIndex;

    public QuestionReuest() {
        this.keySearch = "";
        this.pageIndex = 1;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
