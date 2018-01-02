package vn.bacsiviet.bacsivietvn.AuthFirebase;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import vn.bacsiviet.bacsivietvn.ChatActivity;
import vn.bacsiviet.bacsivietvn.MainActivity;
import vn.bacsiviet.bacsivietvn.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private Button btnSignInPhone, btnSignInEmail, btnSignUp, btnResetPass, btnLogin, btnStartVerify, btnVerifyPhone, btnResend;
    private LoginButton loginButton;
    private EditText edtEmail, edtPassword, edtPhoneNumber, edtVerifyCode;
    private TextInputLayout tilEmail, tilPassword, tilPhoneNumber, tilVerifyCode;
    private TextView tvLoginError;

    private CallbackManager callbackManager;


    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    //phone
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };

        setContentView(R.layout.activity_login);
        // anh xa
        mapWidget();
        //su kien khi nhan button
        onClickListener();

    }


    private void mapWidget() {
        //progressBar loginButton btnSignInPhone btnSignInEmail btnSignUp
        //btnResetPass btnLogin edtEmail edtPassword tilEmail tilPassword tvLoginError
        //  tilPhoneNumber tilVerifyCode
        // edtPhoneNumber  edtVerifyCode
        // btnStartVerify btnVerifyPhone btnResend
        //loginButton
        progressBar = findViewById(R.id.progressBar);
        btnSignInPhone = findViewById(R.id.btn_sign_in_phone);
        btnSignInEmail = findViewById(R.id.btn_sign_in_email);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnResetPass = findViewById(R.id.btn_reset_password);
        btnLogin = findViewById(R.id.btn_login);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        tvLoginError = findViewById(R.id.tv_login_error);
        tilPhoneNumber = findViewById(R.id.til_phone_number);
        tilVerifyCode = findViewById(R.id.til_verify_code);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtVerifyCode = findViewById(R.id.edt_verify_code);
        btnStartVerify = findViewById(R.id.btn_start_verify);
        btnVerifyPhone = findViewById(R.id.btn_verify_phone);
        btnResend = findViewById(R.id.btn_resend);
        loginButton = findViewById(R.id.login_button);
    }

    //su kien khi nhan button
    private void onClickListener() {
        btnLogin.setOnClickListener(this);
        btnSignInPhone.setOnClickListener(this);
        btnSignInEmail.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnResetPass.setOnClickListener(this);
        btnStartVerify.setOnClickListener(this);
        btnVerifyPhone.setOnClickListener(this);
        btnResend.setOnClickListener(this);
        loginButton.setOnClickListener(this);//facebook
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            btnSignInPhone = findViewById(R.id.btn_sign_in_phone);
            case R.id.btn_sign_in_phone:
                enableViews(tilPhoneNumber, edtPhoneNumber, btnStartVerify);
                break;
//            btnSignInEmail = findViewById(R.id.btn_sign_in_email);
            case R.id.btn_sign_in_email:
                enableViews(tilEmail, tilPassword, edtEmail, edtPassword,
                        btnLogin, btnResetPass, btnSignUp);
                break;

//            loginButton = findViewById(R.id.login_button);
            case R.id.login_button:
                //dang nhap facebook
                loginFacebook();
                break;

//            btnSignUp = findViewById(R.id.btn_sign_up);
            case R.id.btn_sign_up:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
                break;

//            btnResetPass = findViewById(R.id.btn_reset_password);
            case R.id.btn_reset_password:
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                finish();
                break;

//            btnLogin = findViewById(R.id.btn_login);
            case R.id.btn_login:
                loginAuthenticateUser();
                break;
//            btnStartVerify = findViewById(R.id.btn_start_verify);
            case R.id.btn_start_verify:
                if (!validatePhoneNumber()) {
                    return;
                }
                startPhoneNumberVerification(edtPhoneNumber.getText().toString());
                break;
//            btnVerifyPhone = findViewById(R.id.btn_verify_phone);
            case R.id.btn_verify_phone:
                String code = edtVerifyCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    edtVerifyCode.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
//            btnResend = findViewById(R.id.btn_resend);
            case R.id.btn_resend:
                resendVerificationCode(edtPhoneNumber.getText().toString(), mResendToken);
                break;


        }

    }

    //dang nhap facebook
    private void loginFacebook() {
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
//        progressBar.setVisibility(View.VISIBLE);
//        loginButton.setVisibility(View.GONE);
        enableViews(progressBar);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @SuppressLint("NewApi")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                disableViews(progressBar);
                enableViews(btnSignInEmail, btnSignInPhone, loginButton);
                if (!task.isSuccessful()) {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        enableView(tvLoginError);
                        tvLoginError.setText(getString(R.string.firebase_error_login));
                        LoginManager.getInstance().logOut();
                    }
                }
//                progressBar.setVisibility(View.GONE);
//                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }


    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // ket thuc dang nhap facebook


    //dang nhap bang dien thoai
    private void phoneCallBacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    edtPhoneNumber.setError("Invalid phone number.");
                    disableViews(progressBar);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    disableViews(progressBar);
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                enableViews(edtVerifyCode, btnVerifyPhone, btnResend);
                disableViews(progressBar);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            goMainScreen();
                            disableViews(progressBar);
                            enableViews(btnSignInEmail, btnSignInPhone, loginButton);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                enableView(tvLoginError);
                                tvLoginError.setText("Invalid code.");
                                edtVerifyCode.setError("Invalid code.");
                            }
                        }

                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        enableView(progressBar);
        phoneCallBacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        enableView(progressBar);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        enableView(progressBar);
        phoneCallBacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = edtPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            edtPhoneNumber.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
    //ket thuc dang nhap bang dien thoai

    //bat dau dang nhap bang gmail/password

    private void loginAuthenticateUser() {
        String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Enter email address!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Enter password!");
            return;
        }

        enableView(progressBar);

        //authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                enableView(tvLoginError);
                                tvLoginError.setText(getString(R.string.minimum_password));

                            } else {
                                enableView(tvLoginError);
                                tvLoginError.setText(getString(R.string.auth_failed));
                            }
                        } else {
                            goMainScreen();
                        }
                        disableViews(progressBar);
                    }
                });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }


    private void enableViews(View... views) {
        //progressBar loginButton btnSignInPhone btnSignInEmail btnSignUp
        //btnResetPass btnLogin edtEmail edtPassword tilEmail tilPassword tvLoginError
        //  tilPhoneNumber tilVerifyCode
        // edtPhoneNumber  edtVerifyCode
        // btnStartVerify btnVerifyPhone btnResend
        //loginButton
        disableViews(loginButton, btnSignInPhone, btnSignInEmail, btnSignUp, btnResetPass, btnLogin,
                edtEmail, edtPassword, tilEmail, tilPassword, tvLoginError,
                tilPhoneNumber, tilVerifyCode, edtPhoneNumber, edtVerifyCode,
                btnStartVerify, btnVerifyPhone, btnResend, loginButton
        );
        for (View v : views) {
            v.setEnabled(true);
            v.setVisibility(View.VISIBLE);
        }
    }

    private void enableView(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
            v.setEnabled(false);
        }
    }


}
