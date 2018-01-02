package vn.bacsiviet.bacsivietvn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.bacsiviet.bacsivietvn.AuthFirebase.LoginActivity;
import vn.bacsiviet.bacsivietvn.FragmentNav.ChatFragment;
import vn.bacsiviet.bacsivietvn.FragmentNav.CommunityFragment;
import vn.bacsiviet.bacsivietvn.FragmentNav.MenuFragment;

public class ChatActivity extends AppCompatActivity {
    //thoi gian tri hoan chuyen trang
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_group:
                    transaction.replace(R.id.frame_layout, new CommunityFragment()).commit();
                    return true;
                case R.id.navigation_help:
//                    transaction.replace(R.id.frame_layout,new CommunityFragment()).commit();
                    return true;
                case R.id.navigation_chat:
                    transaction.replace(R.id.frame_layout,new ChatFragment()).commit();
                    return true;
                case R.id.navigation_notifications:
//                    transaction.replace(R.id.frame_layout,new CommunityFragment()).commit();
                    return true;
                case R.id.navigation_menu:
                    transaction.replace(R.id.frame_layout, new MenuFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            goLoginScreen();
        }
        setContentView(R.layout.activity_chat);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, new CommunityFragment()).commit();
    }

    private void goLoginScreen() {
        //tri hoan chuyen trang
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ChatActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}