package vn.bacsiviet.bacsivietvn;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.bacsiviet.bacsivietvn.Adapter.QuestionAdapter;
import vn.bacsiviet.bacsivietvn.AppService.AppController;
import vn.bacsiviet.bacsivietvn.AppService.Service;
import vn.bacsiviet.bacsivietvn.Model.DataQuestion;
import vn.bacsiviet.bacsivietvn.Model.QuestionReuest;

import static vn.bacsiviet.bacsivietvn.AppService.AppConfig.URL_QUESTION;
import static vn.bacsiviet.bacsivietvn.AppService.AppConfig.URL_QUESTION_DETAIL;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";

    private TextView tvTilte;

    // temporary string to show the parsed response
    private String jsonResponse;

    private ImageView imageBtnFeature;

    private ListView lvQuestion;

    private Button btnQuestion;

    private List<DataQuestion> list = new ArrayList<>();

    private Handler handler;
    private Service service;

    private QuestionReuest questionReuest;
    private  QuestionAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        tvTilte = findViewById(R.id.tv_tilte);
        imageBtnFeature = findViewById(R.id.image_btn_feature);
        lvQuestion = findViewById(R.id.lv_question);
        btnQuestion = findViewById(R.id.btn_question);
        questionReuest = new QuestionReuest();
        LoadPage();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        if (list != null) {
                            adapter = new QuestionAdapter(QuestionActivity.this,R.layout.item_question,list);
                            adapter.notifyDataSetChanged();
                            lvQuestion.setAdapter(adapter);
                            setOnItemlistView();
                        }
                        Log.d(TAG, "handleMessage: ok");
                        break;
                }
            }
        };


    }

    private void setOnItemlistView() {
        lvQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataQuestion news = list.get(i);
                String urlPath = URL_QUESTION_DETAIL+news.getQuestion_url();
                Intent intent = new Intent(QuestionActivity.this,DetailWeb.class);
                intent.putExtra("QUESTION",urlPath);
                startActivity(intent);
            }
        });
    }

    private void LoadPage() {
        final String req = URL_QUESTION + "?page=" + questionReuest.getPageIndex();
        service = new Service(req);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                service.get();
                if (service.isSuccess()) {
                    try {
                        JsonObject data = (JsonObject) new JsonParser().parse(service.getData());
                        Type listType = new TypeToken<List<DataQuestion>>() {}.getType();
                        JSONObject question = new JSONObject(data.getAsJsonObject("question").toString());
                        JSONArray arr =   question.getJSONArray("data") ;
                        for (int i = 0; i < arr.length(); i++){
                            JSONObject resultsObject = arr.getJSONObject(i);
                            DataQuestion dataQuestion = new DataQuestion();
                            dataQuestion.setFullname(resultsObject.getString("fullname"));
                            dataQuestion.setQuestion_title(resultsObject.getString("question_title"));
                            dataQuestion.setQuestion_content(resultsObject.getString("question_content"));
                            dataQuestion.setQuestion_url(resultsObject.getString("question_url"));
                            dataQuestion.setHiding_creator(resultsObject.getInt("hiding_creator"));
                            dataQuestion.setCensor_images(resultsObject.getInt("censor_images"));
                            dataQuestion.setCreated_at(resultsObject.getString("created_at"));
                            list.add(dataQuestion);
                        }
                        handler.sendEmptyMessage(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


}
