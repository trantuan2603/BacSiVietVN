package vn.bacsiviet.bacsivietvn.FragmentDraw;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import vn.bacsiviet.bacsivietvn.ChatActivity;

import vn.bacsiviet.bacsivietvn.QuestionActivity;
import vn.bacsiviet.bacsivietvn.R;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button btnChatFast, btnQuestion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnChatFast = view.findViewById(R.id.btn_chat_fast);
        btnQuestion = view.findViewById(R.id.btn_question_find);

        btnChatFast.setOnClickListener(this);
        btnQuestion.setOnClickListener(this);
        return  view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_chat_fast:
                intentActivity(ChatActivity.class);
                break;
            case R.id.btn_question_find:
                intentActivity(QuestionActivity.class);
                break;
            default:
                Toast.makeText(getActivity(),"Tinh nang dang phat trien",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void intentActivity(Object chatActivityClass) {
        Intent intent = new Intent(getActivity(), (Class<?>) chatActivityClass);
        startActivity(intent);

    }
}
