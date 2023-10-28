package com.example.cusapphotel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cusapphotel.R;
import com.example.cusapphotel.model.Firebase;
import com.example.cusapphotel.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailHistoryOrderActivity extends AppCompatActivity {
    private TextView tv_nameroom,tv_statusroom,tv_paymentMethod, tv_rentdate, tv_returndate, tv_typeroom, tv_priceroom, tv_totalprice, tv_username, tv_numberphone;
    private AppCompatButton btn_cancel;
    private Toolbar toolbar;
    private String currentDate;
    private Order mcurrentOrder;
    private Firebase mfirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_order);
        mcurrentOrder = (Order) getIntent().getSerializableExtra("currentOrder");
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

    private void getData() {
        tv_nameroom.setText(mcurrentOrder.getRoomName());
        tv_priceroom.setText(String.format("%,d", Math.round(mcurrentOrder.getRoomPrice())) + " VNĐ/Ngày");
        tv_typeroom.setText("Loại xe: " + mcurrentOrder.getRoomType());
        currentDate = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        tv_rentdate.setText(mcurrentOrder.getRentDate());
        tv_returndate.setText(mcurrentOrder.getReturnDate());
        tv_totalprice.setText(String.format("%,d", Math.round(mcurrentOrder.getTotalprice())) + " VNĐ");
        tv_numberphone.setText(mcurrentOrder.getRenterPhone());
        tv_username.setText(mcurrentOrder.getRenterName());
        tv_statusroom.setText("Trạng thái: " + mcurrentOrder.getOrderStatus());
        tv_paymentMethod.setText(mcurrentOrder.getPaymentMethod());
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbardetailhistoryorder);
        tv_nameroom = findViewById(R.id.tv_nameroom);
        tv_statusroom = findViewById(R.id.tv_statusroom);
        tv_paymentMethod = findViewById(R.id.tv_paymentMethod);
        tv_rentdate = findViewById(R.id.tv_rentdate);
        tv_returndate = findViewById(R.id.tv_returndate);
        tv_typeroom = findViewById(R.id.tv_typeroom);
        tv_priceroom = findViewById(R.id.tv_priceroom);
        tv_totalprice = findViewById(R.id.tv_totalprice);
        tv_username = findViewById(R.id.tv_username);
        tv_numberphone = findViewById(R.id.tv_numberphone);
        btn_cancel = findViewById(R.id.btn_cancel);
        mfirebase = new Firebase(this);
    }
}