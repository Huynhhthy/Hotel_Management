package com.example.cusapphotel.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cusapphotel.R;
import com.example.cusapphotel.model.Loginstatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {
    EditText edt_otp;
    TextView send_OTP_again;
    String mphonenumber, mpassword, mverifyID,codeOTP;
    AppCompatButton btn_confirm;
    private PhoneAuthProvider.ForceResendingToken mForceResendingToken;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otpactivity);

        Anhxa();
        getDataIntent();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeOTP = edt_otp.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mverifyID, codeOTP);
                signInWithPhoneAuthCredential(credential);
            }
        });
        send_OTP_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mphonenumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(SendOTPActivity.this)
                        .setForceResendingToken(mForceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }
                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(SendOTPActivity.this, "VerificationFailed", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String verifyID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifyID, forceResendingToken);
                                mverifyID = verifyID;
                                mForceResendingToken = forceResendingToken;
                            }
                        })
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options );
            }
        });
    }
    private void Anhxa() {
        edt_otp = findViewById(R.id.edt_otp);
        btn_confirm = findViewById(R.id.btn_confirm);
        send_OTP_again = findViewById(R.id.send_otp_again);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }
    private void getDataIntent(){
        mphonenumber = getIntent().getExtras().getString("phonenumber");
        mpassword = getIntent().getExtras().getString("password");
        mverifyID = getIntent().getExtras().getString("verifyID");
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            String userID = user.getUid();

                            // Lưu userID và password vào Firestore
                            saveUserToFirestore(userID, mpassword,mphonenumber);
                            gotoTrangChuActivity(userID);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(SendOTPActivity.this, "Mã OTP không chính xác", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void saveUserToFirestore(String userID, String password, String phonenumber) {
        Map<String, Object> user = new HashMap<>();
        user.put("password", password);
        user.put("numberphone", phonenumber);

        mFirestore.collection("users").document(userID).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User document added with ID: " + userID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding user document", e);
                    }
                });
    }
    private void gotoTrangChuActivity(String userID) {
        Loginstatus.getInstance().setLoggedIn(true);
        Loginstatus.getInstance().setUserID(userID);
        Intent i = new Intent(SendOTPActivity.this, TrangchuActivity.class);
        startActivity(i);
    }
}