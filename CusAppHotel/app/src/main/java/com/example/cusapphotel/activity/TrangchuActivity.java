package com.example.cusapphotel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.cusapphotel.R;
import com.example.cusapphotel.adapter.Adapterviewpager;
import com.example.cusapphotel.adapter.RcvListRoomAdapter;
import com.example.cusapphotel.model.Firebase;
import com.example.cusapphotel.model.Loginstatus;
import com.example.cusapphotel.model.Room;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TrangchuActivity extends AppCompatActivity {

    int currentPage = 0;
    ViewPager viewPager;
    private  String userID;
    private RecyclerView rcvphongdon,rcvphongdoi;
    private RcvListRoomAdapter adapter1, adapter2;
    private ArrayList<Room> phongdonlist;
    private ArrayList<Room> phongdoilist;
    private Firebase firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        Anhxa();
        setAutoScrollViewScroll();
        setDataForRcv();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDataForRcv();
    }

    private void Anhxa() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Adapterviewpager adapterviewpager = new Adapterviewpager(this);
        viewPager.setAdapter(adapterviewpager);
        viewPager.setClipToOutline(true);
        rcvphongdon = findViewById(R.id.rcv_phongdon);
        rcvphongdoi = findViewById(R.id.rcv_phongdoi);
        firebase = new Firebase(this);
    }
    public void detail_account(View view){
        if (Loginstatus.getInstance().isLoggedIn() == true){
            Intent i = new Intent(TrangchuActivity.this, DetailAccountActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(TrangchuActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
    private void setAutoScrollViewScroll() {
        Timer timer;
        final long DELAY_MS = 500;
        final long PERIOD_MS = 2000;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 4) currentPage = 0;
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },DELAY_MS, PERIOD_MS);
    }
    private void setDataForRcv(){
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvphongdon.setLayoutManager(layoutManager1);
        rcvphongdoi.setLayoutManager(layoutManager2);
        firebase.getAllPhongDon(new Firebase.FirebaseCallback<Room>() {
            @Override
            public void onCallback(ArrayList<Room> list1) {
                phongdonlist = list1;
                adapter1 = new RcvListRoomAdapter(TrangchuActivity.this, phongdonlist);
                rcvphongdon.setAdapter(adapter1);
            }
        });
        firebase.getAllPhongDoi(new Firebase.FirebaseCallback<Room>() {
            @Override
            public void onCallback(ArrayList<Room> list2) {
                phongdoilist = list2;
                adapter2 = new RcvListRoomAdapter(TrangchuActivity.this, phongdoilist);
                rcvphongdoi.setAdapter(adapter2);
            }
        });
    }
}