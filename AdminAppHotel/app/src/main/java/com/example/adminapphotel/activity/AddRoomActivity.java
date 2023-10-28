package com.example.adminapphotel.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adminapphotel.R;
import com.example.adminapphotel.model.Firebase;
import com.example.adminapphotel.model.Room;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class AddRoomActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_1 = 1;
    private static final int PICK_IMAGE_REQUEST_2 = 2;
    private static final int PICK_IMAGE_REQUEST_3 = 3;
    private EditText edt_nameroom, edt_priceroom, edt_descriptionroom;
    private Spinner spinner_typeroom, spinner_statusroom;
    private ImageView img_room1, img_room2, img_room3;
    private AppCompatButton btn_comfirm;
    private Toolbar toolbar;
    private Firebase mfirebase;
    private Uri Image1, Image2, Image3;
    private Room room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        Anhxa();
        setdata();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        img_room1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_IMAGE_REQUEST_1);
            }
        });
        img_room2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_IMAGE_REQUEST_2);
            }
        });
        img_room3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_IMAGE_REQUEST_3);
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCar();
            }
        });
    }
    private void Anhxa() {
        toolbar = findViewById(R.id.toolbaraddroom);
        edt_nameroom = findViewById(R.id.edt_nameroom);
        edt_priceroom = findViewById(R.id.edt_priceroom);
        edt_descriptionroom = findViewById(R.id.edt_descriptionroom);
        spinner_typeroom = findViewById(R.id.spinner_typeroom);
        spinner_statusroom = findViewById(R.id.spinner_statusroom);
        img_room1 = findViewById(R.id.img_room1);
        img_room2 = findViewById(R.id.img_room2);
        img_room3 = findViewById(R.id.img_room3);
        btn_comfirm = findViewById(R.id.btn_confirm);
        mfirebase = new Firebase(this);
    }
    private void openFileChooser(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            switch(requestCode) {
                case PICK_IMAGE_REQUEST_1:
                    Image1 = data.getData();
                    Glide.with(this).load(Image1).into(img_room1);
                    break;
                case PICK_IMAGE_REQUEST_2:
                    Image2 = data.getData();
                    Glide.with(this).load(Image2).into(img_room2);
                    break;
                case PICK_IMAGE_REQUEST_3:
                    Image3 = data.getData();
                    Glide.with(this).load(Image3).into(img_room3);
                    break;
            }
        }
    }
    private void setdata() {
        ArrayList<String> typelist = new ArrayList<String>();
        typelist.add("Phòng Đơn");
        typelist.add("Phòng Đôi");
        ArrayAdapter adapterspinnertypecar = new ArrayAdapter(this, android.R.layout.simple_spinner_item, typelist);
        spinner_typeroom.setAdapter(adapterspinnertypecar);

        ArrayList<String> statuslist = new ArrayList<String>();
        statuslist.add("Còn Trống");
        statuslist.add("Đã thuê");
        ArrayAdapter adapterspinnerstatusar = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statuslist);
        spinner_statusroom.setAdapter(adapterspinnerstatusar);

    }
    private void addCar(){
        String name = edt_nameroom.getText().toString().trim();
        String priceString = edt_priceroom.getText().toString().trim();
        String description = edt_descriptionroom.getText().toString().trim();
        String type = spinner_typeroom.getSelectedItem().toString();
        String status = spinner_statusroom.getSelectedItem().toString();

        if (name.isEmpty() || priceString.isEmpty() || description.isEmpty() || type.isEmpty() || status.isEmpty()) {
            Toast.makeText(AddRoomActivity.this, "Hãy điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }
        double price = Double.parseDouble(priceString);

        room = new Room();
        room.setNameroom(name);
        room.setPriceroom(price);
        room.setDescriptionroom(description);
        room.setTyperoom(type);
        room.setStatusroom(status);

        List<Uri> list = new ArrayList<>();
        if (Image1 != null) list.add(Image1);
        if (Image2 != null) list.add(Image2);
        if (Image3 != null) list.add(Image3);

        mfirebase.addCar(room, list,new Firebase.AddCarCallback(){
            @Override
            public void onCallback(boolean isSuccess) {
                if(isSuccess) {
                    Toast.makeText(AddRoomActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                } else {
                    Toast.makeText(AddRoomActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}