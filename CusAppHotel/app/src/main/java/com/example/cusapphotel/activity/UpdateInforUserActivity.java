package com.example.cusapphotel.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cusapphotel.R;
import com.example.cusapphotel.model.Firebase;
import com.example.cusapphotel.model.Loginstatus;
import com.example.cusapphotel.model.User;

import java.util.Calendar;

public class UpdateInforUserActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private EditText edt_username, edt_address, edt_cccd;
    private TextView tv_numberphone, tv_dateofbirth;
    private ImageView mImgAvatar;
    private RadioGroup rg_gioitinh;
    private RadioButton radioButtonNam, radioButtonNu;
    private AppCompatButton btn_update;
    private Toolbar toolbar;
    private String userID;
    private User user;
    private Firebase mfirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_infor_user);

        Anhxa();
        getin4user();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        tv_dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogchonngaysinh();
            }
        });
        mImgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarupdateinforuser);
        mImgAvatar = findViewById(R.id.img_avatar);
        edt_username = findViewById(R.id.edt_username);
        tv_numberphone = findViewById(R.id.tv_numberphone);
        edt_address = findViewById(R.id.edt_address);
        edt_cccd = findViewById(R.id.edt_cccd);
        tv_dateofbirth = findViewById(R.id.tv_dateofbirth);
        rg_gioitinh = findViewById(R.id.rg_gioitinh);
        radioButtonNam = findViewById(R.id.rb_nam);
        radioButtonNu = findViewById(R.id.rb_nu);
        btn_update = findViewById(R.id.btn_update);
        userID = Loginstatus.getInstance().getUserID();
        mfirebase = new Firebase(this);
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Glide.with(this).load(mImageUri).into(mImgAvatar);
        }
    }
    private void getin4user() {
        mfirebase.getInforUserByUserId(userID, new Firebase.UserCallback() {
            @Override
            public void onCallback(User muser) {
                user = muser;
                setin4user();
            }
        });
    }
    private void setin4user() {
        tv_numberphone.setText(user.getNumberphone());
        if(user.getUsername() != null) edt_username.setText(user.getUsername());
        if(user.getUseravatar() != null) Glide.with(UpdateInforUserActivity.this).load(user.getUseravatar()).into(mImgAvatar);
        if(user.getAddress() != null) edt_address.setText(user.getAddress());
        if(user.getCccd() != null) edt_cccd.setText(user.getCccd());
        if(user.getDateofbirth() != null) tv_dateofbirth.setText(user.getDateofbirth());
        if(user.getGioitinh() != null){
            if (!user.getGioitinh().equals("Nữ")) radioButtonNam.setChecked(true);
            else radioButtonNu.setChecked(true);
        }
    }
    private void updateInfo(){
        String username = edt_username.getText().toString();
        String address = edt_address.getText().toString();
        String cccd = edt_cccd.getText().toString();
        String dateofbirth = tv_dateofbirth.getText().toString();
        int selectedId = rg_gioitinh.getCheckedRadioButtonId();
        String gioitinh = (selectedId == R.id.rb_nam) ? "Nam" : "Nữ";

        if (username.isEmpty() || address.isEmpty() || cccd.isEmpty() || dateofbirth.isEmpty() || gioitinh.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thon tin", Toast.LENGTH_SHORT).show();
        }else if(cccd.length() != 12) Toast.makeText(this, "Giấy phép lái xe không hợp lệ", Toast.LENGTH_SHORT).show();

        if (mImageUri == null){
            mfirebase.updateUserInfo(userID ,username, address, cccd,dateofbirth,gioitinh, new Firebase.UpdateUserCallback() {
                @Override
                public void onCallback(boolean isSuccess) {
                    if(isSuccess) {
                        Toast.makeText(UpdateInforUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        getin4user();
                    } else {
                        Toast.makeText(UpdateInforUserActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            mfirebase.updateUserInfoAndImage(userID ,username, address, cccd,dateofbirth,gioitinh, mImageUri, new Firebase.UpdateUserCallback() {
                @Override
                public void onCallback(boolean isSuccess) {
                    if(isSuccess) {
                        Toast.makeText(UpdateInforUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        getin4user();
                    } else {
                        Toast.makeText(UpdateInforUserActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void showdialogchonngaysinh(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog DateOfBirthPickerDialog = new DatePickerDialog(UpdateInforUserActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateofbirth = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        tv_dateofbirth.setText(dateofbirth);
                    }
                }, mYear, mMonth, mDay);
        DateOfBirthPickerDialog.show();
    }
}