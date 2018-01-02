package vn.bacsiviet.bacsivietvn;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.bacsiviet.bacsivietvn.AuthFirebase.LoginActivity;
import vn.bacsiviet.bacsivietvn.FragmentDraw.HomeFragment;
import vn.bacsiviet.bacsivietvn.Helper.SQLiteHandler;
import vn.bacsiviet.bacsivietvn.Helper.SessionManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView profileImage;
    private TextView nameTextView;
    private TextView emailTextView;
    private LinearLayout llAccount;
    private Button btnSignIn;
    private String name, email, mAvatar;
    private Uri photoUrl;
    private FirebaseUser user;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    private SQLiteHandler db;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        user = FirebaseAuth.getInstance().getCurrentUser();
        checkAndRequestPermissions();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //hien fragment Home
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
       transaction.replace(R.id.frame_container,homeFragment);
        transaction.commit();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //anh xa
        View v = navigationView.getHeaderView(0);
        btnSignIn = v.findViewById(R.id.btn_login);
        profileImage = v.findViewById(R.id.imageView);
        nameTextView = v.findViewById(R.id.tv_account);
        emailTextView = v.findViewById(R.id.tv_email);
        llAccount = v.findViewById(R.id.ll_account);

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        if (user != null) {
            // name = user.getDisplayName();
            //email = user.getEmail();
            //photoUrl = user.getPhotoUrl();
            name = user.get("fullname");
            email = user.get("email");
            mAvatar = user.get("avatar");


            try {
                URL url = new URL(mAvatar);
                Picasso.with(this)
                        .load(String.valueOf(url))
                        .resize(64, 64)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher)
                        .into(profileImage);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            llAccount.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);


            if (TextUtils.isEmpty(name)) {
                nameTextView.setVisibility(View.GONE);
            } else {
                nameTextView.setText(name);
                nameTextView.setVisibility(View.VISIBLE);
            }
            emailTextView.setText(email);
        } else {
            llAccount.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
        }
        if (btnSignIn != null) {
            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(MainActivity.this, LoginServerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }


    }

    // xin quyen
    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(getApplicationContext(), "chuc nang dang phat trien", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_question) {
            Toast.makeText(getApplicationContext(), "chuc nang dang phat trien", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_post) {
            Toast.makeText(getApplicationContext(), "chuc nang dang phat trien", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_sick) {
            Toast.makeText(getApplicationContext(), "chuc nang dang phat trien", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_drug) {
            Toast.makeText(getApplicationContext(), "chuc nang dang phat trien", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_doctor) {
            Toast.makeText(getApplicationContext(), "chuc nang dang phat trien", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_health_service) {
            Toast.makeText(getApplicationContext(), "chuc nang dang phat trien", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_sale) {
            Toast.makeText(getApplicationContext(), "chuc nang dang phat trien", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_info) {
            Toast.makeText(getApplicationContext(), "chuc nang dang phat trien", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_logout) {
                //logout(navigationView);
                logoutUser();
                llAccount.setVisibility(View.GONE);
                btnSignIn.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this," dang xuat thanh cong",Toast.LENGTH_LONG).show();

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginServerActivity.class);
        startActivity(intent);

    }
}
