package vn.bacsiviet.bacsivietvn.Adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.bacsiviet.bacsivietvn.Model.DataQuestion;
import vn.bacsiviet.bacsivietvn.R;

/**
 * Created by Administrator on 14/12/2017.
 */

public class QuestionAdapter extends BaseAdapter {
    private static final String TAG = "NewsAdapter";
    Context context;
    int layout;
    List<DataQuestion> newsList;

    public QuestionAdapter(Context context, int list_view, List<DataQuestion> newsList) {
        this.context = context;
        this.layout = list_view;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int i) {
        return newsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView tvTitle, tvPerson, tvDate, tvGroup, tvLike, tvContentQuestion;


        public ViewHolder(View view) {
            tvTitle = view.findViewById(R.id.tv_title);
            tvPerson = view.findViewById(R.id.tv_person);
            tvDate = view.findViewById(R.id.tv_date);
            tvGroup = view.findViewById(R.id.tv_group);
            tvContentQuestion = view.findViewById(R.id.tv_content_question);
            tvLike = view.findViewById(R.id.tv_like);


        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DataQuestion news = newsList.get(i);
        String title = news.getQuestion_title();
        viewHolder.tvTitle.setText(news.getQuestion_title());


        String person = news.getFullname();
        if(!person.isEmpty()) viewHolder.tvPerson.setText(person);

        String date = news.getCreated_at();
        if(!date.isEmpty()) viewHolder.tvDate.setText(date);

        String cntent = news.getQuestion_content();
        if(!cntent.isEmpty()) viewHolder.tvContentQuestion.setText(cntent);


        String mlike = String.valueOf(news.getHiding_creator());
        if(!mlike.isEmpty()) viewHolder.tvLike.setText(mlike);

        return view;
    }
}

