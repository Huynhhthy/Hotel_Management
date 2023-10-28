package com.example.adminapphotel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.adminapphotel.R;
import com.example.adminapphotel.model.Firebase;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_email, edt_password;
    AppCompatButton btn_login;
    Firebase mfirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Anhxa();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                mfirebase.signIn(email, password, new Firebase.SignInCallback() {
                    @Override
                    public void onCallback(boolean isSuccess, String adminID) {
                        if (isSuccess) {
                            gotoTrangChuActivity(adminID);
                        } else {
                            Toast.makeText(LoginActivity.this, "Tài khoàn không chính xác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void Anhxa() {
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        mfirebase = new Firebase(this);
    }

    private void gotoTrangChuActivity(String adminID) {
        Intent i = new Intent(LoginActivity.this, TrangChuActivity.class);
        i.putExtra("adminID", adminID);
        startActivity(i);
    }
}