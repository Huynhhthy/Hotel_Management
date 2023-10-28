package com.example.cusapphotel.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_phonenumber,edt_password, edt_comfirmpassword;
    TextView tv_noti;
    Button btn_register;
    String mphonenumber, mpassword, mcomfirmpassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Anhxa();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mphonenumber = edt_phonenumber.getText().toString().trim();
                mpassword = edt_password.getText().toString().trim();
                mcomfirmpassword = edt_comfirmpassword.getText().toString().trim();
                if (!mcomfirmpassword.equals(mpassword)) {
                    tv_noti.setText("Mật khẩu không trùng khớp!!");
                    return;
                }else {
                    checkPhoneNumberExists(mphonenumber, mpassword);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        tv_noti.setText("");
    }

    private void Anhxa() {
        edt_phonenumber = findViewById(R.id.edt_phonenumber);
        edt_password = findViewById(R.id.edt_password);
        edt_comfirmpassword = findViewById(R.id.edt_comfirmpassword);
        btn_register = findViewById(R.id.btn_register);
        tv_noti = findViewById(R.id.tv_noti);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }
    private void checkPhoneNumberExists(String phonenumber, String password) {
        CollectionReference usersRef = mFirestore.collection("users");

        usersRef.whereEqualTo("numberphone", phonenumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Số điện thoại đã tồn tại trong Firestore
                                // Xử lý tại đây
                                tv_noti.setText("Số điện thoại đã tồn tại!!!");
                            } else {
                                registerWithPhoneNumber(phonenumber, password);
                            }
                        } else {
                            // Xảy ra lỗi khi kiểm tra số điện thoại
                            Toast.makeText(RegisterActivity.this, "Lỗi khi kiểm tra số điện thoại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void registerWithPhoneNumber(String phonenumber, String password){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phonenumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(RegisterActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(RegisterActivity.this, "VerificationFailed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verifyID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyID, forceResendingToken);
                        gotoSendOTPActivity(verifyID,phonenumber,password);
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options );
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
                            // Update UI
                            String userID = user.getUid();

                            // Lưu userID và password vào Firestore
                            saveUserIDAndPasswordAndPhoneNumber(userID, mpassword,mphonenumber);
                            gotoTrangChuActivity(userID);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void saveUserIDAndPasswordAndPhoneNumber(String userID, String password, String phonenumber) {
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
        Intent i = new Intent(RegisterActivity.this,TrangchuActivity.class);
        startActivity(i);
    }
    private void gotoSendOTPActivity(String verifyID,String phonenumber,String password) {
        Intent i = new Intent(RegisterActivity.this,SendOTPActivity.class);
        i.putExtra("phonenumber", phonenumber);
        i.putExtra("password", password);
        i.putExtra("verifyID", verifyID);
        startActivity(i);
    }

    public void back_home(View view) {
        onBackPressed();
        finish();
    }
}