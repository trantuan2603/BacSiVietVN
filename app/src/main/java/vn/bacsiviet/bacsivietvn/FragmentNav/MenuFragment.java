package vn.bacsiviet.bacsivietvn.FragmentNav;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import vn.bacsiviet.bacsivietvn.AuthFirebase.LoginActivity;
import vn.bacsiviet.bacsivietvn.R;

public class MenuFragment extends Fragment implements View.OnClickListener {
    private Button btnSignOut;
    private ImageView profileImage;
    private TextView nameTextView;
    private TextView emailTextView;
    private String name, email;
    private Uri photoUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
             name = user.getDisplayName();
             email = user.getEmail();
             photoUrl = user.getPhotoUrl();
        } else {
            goLoginScreen();
        }
    }

    private void goLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        //anh xa
        btnSignOut = view.findViewById(R.id.btn_sign_out);
        profileImage = view.findViewById(R.id.profile_image);
        nameTextView = view.findViewById(R.id.tv_name_facebook);
        emailTextView = view.findViewById(R.id.tv_email);

        Picasso.with(getContext())
                .load(photoUrl)
                .resize(48, 48)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(profileImage);
        nameTextView.setText(name);
        emailTextView.setText(email);
        btnSignOut.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case   R.id.btn_sign_out:
                logout(view);
                break;
        }
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}
