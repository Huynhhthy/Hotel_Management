package com.example.adminapphotel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.adminapphotel.R;
import com.example.adminapphotel.adapter.VpgImageRoomAdapter;
import com.example.adminapphotel.model.Room;

import java.util.ArrayList;

public class DetailRoomActivity extends AppCompatActivity {
    private TextView tv_nameroom, tv_priceroom, tv_typeroom, tv_statusroom, tv_describeroom;
    private ViewPager vpg_detailroom;
    private Room currentRoom;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);
        currentRoom = (Room) getIntent().getSerializableExtra("currentRoom");
        Anhxa();
        setdata();
    }
    private void setdata() {
        tv_nameroom.setText(currentRoom.getNameroom());
        tv_priceroom.setText(String.format("%,d", Math.round(currentRoom.getPriceroom())) + " VNĐ/Ngày");
        tv_typeroom.setText("Loại xe: "+ currentRoom.getTyperoom());
        tv_statusroom.setText("Tình trạng: "+ currentRoom.getStatusroom());
        tv_describeroom.setText(currentRoom.getDescriptionroom());
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(currentRoom.getImage1());
        imageUrls.add(currentRoom.getImage2());
        imageUrls.add(currentRoom.getImage3());
        VpgImageRoomAdapter adapter = new VpgImageRoomAdapter(this, imageUrls);
        vpg_detailroom.setAdapter(adapter);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }


    private void Anhxa() {
        toolbar = findViewById(R.id.toolbardetailcar);
        tv_nameroom = findViewById(R.id.tv_nameroom);
        tv_priceroom = findViewById(R.id.tv_priceroom);
        tv_typeroom = findViewById(R.id.tv_typeroom);
        tv_statusroom = findViewById(R.id.tv_statusroom);
        tv_describeroom = findViewById(R.id.tv_describeroom);
        vpg_detailroom = findViewById(R.id.vpg_detailroom);
    }
}