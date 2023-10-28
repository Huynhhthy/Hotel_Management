package com.example.cusapphotel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.cusapphotel.R;
import com.example.cusapphotel.adapter.RcvHistoryOrderAdapter;
import com.example.cusapphotel.model.Firebase;
import com.example.cusapphotel.model.Loginstatus;
import com.example.cusapphotel.model.Order;

import java.util.ArrayList;

public class HistoryOrderActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rcvAllOrders;
    private RcvHistoryOrderAdapter adapter;
    private ArrayList<Order> ordersList;
    private Firebase mfirebase;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);

        Anhxa();
        getData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarhistoryorders);
        rcvAllOrders = findViewById(R.id.rcv_historyorders);
        ordersList = new ArrayList<>();
        userID = Loginstatus.getInstance().getUserID();
        mfirebase = new Firebase(this);
    }
    private void getData(){
        mfirebase.getAllHistoryOrders(userID, new Firebase.getAllHistoryOrdersCallback() {
            @Override
            public void onCallback(ArrayList<Order> ordersList) {
                rcvAllOrders.setLayoutManager(new LinearLayoutManager(HistoryOrderActivity.this));
                adapter = new RcvHistoryOrderAdapter(HistoryOrderActivity.this,ordersList);
                rcvAllOrders.setAdapter(adapter);
            }
        });
    }
}