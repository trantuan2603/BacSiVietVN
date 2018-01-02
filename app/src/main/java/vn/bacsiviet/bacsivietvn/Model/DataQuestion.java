package vn.bacsiviet.bacsivietvn.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 14/12/2017.
 */

public class DataQuestion implements Serializable {

   private String fullname;
   private String question_title;
   private String question_content;
   private String question_url;
   private int hiding_creator;
   private int censor_images;
   private String created_at;

    public DataQuestion() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public String getQuestion_url() {
        return question_url;
    }

    public void setQuestion_url(String question_url) {
        this.question_url = question_url;
    }

    public int getHiding_creator() {
        return hiding_creator;
    }

    public void setHiding_creator(int hiding_creator) {
        this.hiding_creator = hiding_creator;
    }

    public int getCensor_images() {
        return censor_images;
    }

    public void setCensor_images(int censor_images) {
        this.censor_images = censor_images;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
