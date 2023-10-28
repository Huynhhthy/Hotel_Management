package com.example.cusapphotel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.cusapphotel.R;
import com.example.cusapphotel.adapter.RcvOrderAdapter;
import com.example.cusapphotel.model.Firebase;
import com.example.cusapphotel.model.Loginstatus;
import com.example.cusapphotel.model.Order;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rcvAllOrders;
    private RcvOrderAdapter adapter;
    private ArrayList<Order> ordersList;
    private Firebase mfirebase;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

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
        toolbar = findViewById(R.id.toolbarorders);
        rcvAllOrders = findViewById(R.id.rcv_orders);
        ordersList = new ArrayList<>();
        userID = Loginstatus.getInstance().getUserID();
        mfirebase = new Firebase(this);
    }
    private void getData(){
        mfirebase.getAllOrders(userID, new Firebase.getAllHistoryOrdersCallback() {
            @Override
            public void onCallback(ArrayList<Order> ordersList) {
                rcvAllOrders.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
                adapter = new RcvOrderAdapter(OrdersActivity.this,ordersList);
                rcvAllOrders.setAdapter(adapter);
            }
        });
    }
}