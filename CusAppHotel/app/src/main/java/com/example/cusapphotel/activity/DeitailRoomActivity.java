package com.example.cusapphotel.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cusapphotel.R;
import com.example.cusapphotel.adapter.VpgImageCarAdapter;
import com.example.cusapphotel.model.Firebase;
import com.example.cusapphotel.model.Loginstatus;
import com.example.cusapphotel.model.Room;
import com.example.cusapphotel.model.User;

import java.util.ArrayList;

public class DeitailRoomActivity extends AppCompatActivity {
    private TextView tv_nameroom, tv_priceroom, tv_typeroom, tv_statusroom, tv_describeroom;
    private ViewPager vpg_detailcar;
    private Room currentRoom;
    private Toolbar toolbar;
    private AppCompatButton btn_thuexe;
    private String userID;
    private User user;
    private Firebase mfirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deitail_room);
        currentRoom = (Room) getIntent().getSerializableExtra("currentRoom");
        Anhxa();
        setdata();
        getin4user();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        btn_thuexe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Loginstatus.getInstance().isLoggedIn() == true){
                    if (user.getCccd() != null ){
                        Intent i = new Intent(DeitailRoomActivity.this, OrderingActivity.class);
                        i.putExtra("currentRoom", currentRoom);
                        startActivity(i);
                    }else showDialogComfirmGPLX();
                }
                else {
                    Toast.makeText(DeitailRoomActivity.this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(DeitailRoomActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }
    private void setdata() {
        tv_nameroom.setText(currentRoom.getNameroom());
        tv_priceroom.setText(String.format("%,d", Math.round(currentRoom.getPriceroom())) + " VNĐ/Ngày");
        tv_typeroom.setText("Loại phòng: "+ currentRoom.getTyperoom());
        tv_statusroom.setText("Tình trạng: "+ currentRoom.getStatusroom());
        tv_describeroom.setText(currentRoom.getDescriptionroom());
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(currentRoom.getImage1());
        imageUrls.add(currentRoom.getImage2());
        imageUrls.add(currentRoom.getImage3());
        VpgImageCarAdapter adapter = new VpgImageCarAdapter(this, imageUrls);
        vpg_detailcar.setAdapter(adapter);
        if(Loginstatus.getInstance().isLoggedIn() == true){
            userID = Loginstatus.getInstance().getUserID();
        }
        mfirebase = new Firebase(this);
    }
    private void Anhxa() {
        toolbar = findViewById(R.id.toolbardetailroom);
        tv_nameroom = findViewById(R.id.tv_nameroom);
        tv_priceroom= findViewById(R.id.tv_priceroom);
        tv_typeroom = findViewById(R.id.tv_typeroom);
        tv_statusroom = findViewById(R.id.tv_statuscar);
        tv_describeroom = findViewById(R.id.tv_describeroom);
        vpg_detailcar = findViewById(R.id.vpg_detailroom);
        btn_thuexe = findViewById(R.id.btn_thuephong);
    }
    private void showDialogComfirmGPLX(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DeitailRoomActivity.this);
        View dialogView = LayoutInflater.from(DeitailRoomActivity.this).inflate(R.layout.dialog_comfirmgplx, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        AppCompatButton btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        AppCompatButton btn_confirm = dialogView.findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DeitailRoomActivity.this, UpdateInforUserActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void getin4user() {
        if(userID != null){
            mfirebase.getInforUserByUserId(userID, new Firebase.UserCallback() {
                @Override
                public void onCallback(User muser) {
                    user = muser;
                }
            });
        }
    }
}