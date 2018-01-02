package vn.bacsiviet.bacsivietvn.AuthFirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import vn.bacsiviet.bacsivietvn.ChatActivity;
import vn.bacsiviet.bacsivietvn.R;

public class SignupActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnRegister, btnSignIn, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mapWidget();
        onClickListener();
    }

    private void onClickListener() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, ChatActivity.class));
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setText("");
                tvError.setVisibility(View.GONE);
                createUserFire();
            }
        });
    }

    private void createUserFire() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("Enter email address!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("Enter password!");
            return;
        }

        if (password.length() < 6) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("Password too short, enter minimum 6 characters!");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            tvError.setVisibility(View.VISIBLE);
                            tvError.setText("Register fail, " +task.getException().getMessage());
                        } else {
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });
    }

    // tham chieu cac ui sign up
    private void mapWidget() {
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        btnRegister = findViewById(R.id.btn_register);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tv_error);

    }
}