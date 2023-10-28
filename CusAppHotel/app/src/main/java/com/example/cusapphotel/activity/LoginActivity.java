package com.example.cusapphotel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cusapphotel.R;
import com.example.cusapphotel.model.Firebase;
import com.example.cusapphotel.model.Loginstatus;

public class LoginActivity extends AppCompatActivity {
    AppCompatButton btn_login, btn_register;
    EditText edt_numberphone, edt_password;
    String mnumberphone, mpassword;
    Firebase mfirebase;
    ImageButton btn_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Anhxa();

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mnumberphone = edt_numberphone.getText().toString().trim();
                mpassword = edt_password.getText().toString().trim();
                // Kiểm tra xem người dùng đã nhập email hoặc số điện thoại và mật khẩu hay chưa
                if (mnumberphone.isEmpty() || mpassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập email/số điện thoại và mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                login(mnumberphone, mpassword);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Anhxa() {
        btn_home = findViewById(R.id.btn_home);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        edt_numberphone = findViewById(R.id.edt_numberphone);
        edt_password = findViewById(R.id.edt_password);
        mfirebase = new Firebase(this);
    }
    private void gotoTrangChuActivity(String userID) {
        Loginstatus.getInstance().setLoggedIn(true);
        Loginstatus.getInstance().setUserID(userID);
        Intent i = new Intent(LoginActivity.this, TrangchuActivity.class);
        startActivity(i);
    }
    private void login(String phoneNumber, String password){
        mfirebase.loginWithPhoneNumber(mnumberphone, mpassword, new Firebase.LoginCallback() {
            @Override
            public void onCallback(boolean isSuccess, String userId) {
                if (isSuccess) {
                    gotoTrangChuActivity(userId);
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Vui lòng kiểm tra lại số điện thoại và mật khẩu.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}