package com.example.cusapphotel.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cusapphotel.R;
import com.example.cusapphotel.model.Firebase;
import com.example.cusapphotel.model.Loginstatus;
import com.example.cusapphotel.model.Order;
import com.example.cusapphotel.model.Room;
import com.example.cusapphotel.model.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrderingActivity extends AppCompatActivity {
    private TextView tv_nameroom, tv_rentdate, tv_returndate, tv_typeroom, tv_priceroom, tv_totalprice, tv_username, tv_numberphone;
    private ImageButton btn_rentdate, btn_returndate;
    private RadioGroup rgPaymentMethor;
    private String returnDate,rentDate;
    private String currentDate;
    private LocalDate curdate;
    private double totalprice;
    private AppCompatButton btn_comfirm;
    private Room currentRoom;
    private Toolbar toolbar;
    private String userID;
    private User user;
    private Order order;
    private Firebase mfirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering);
        currentRoom = (Room) getIntent().getSerializableExtra("currentRoom");
        Anhxa();
        getin4user();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        btn_rentdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogrentdate();
            }
        });
        btn_returndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogreturndate();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int paymentMethodId = rgPaymentMethor.getCheckedRadioButtonId();
                String paymentMethod = "";
                if (paymentMethodId == R.id.rb_cod) {
                    paymentMethod = "COD";
                } else if (paymentMethodId == R.id.rb_atm) {
                    paymentMethod = "ATM";
                } else if (paymentMethodId == R.id.rb_momo) {
                    paymentMethod = "MoMo";
                }
                String orderStatus = "Đang chờ xác nhận";
                if (rentDate == null || returnDate == null || paymentMethod.equals("")){
                    Toast.makeText(OrderingActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    order = new Order(userID, currentDate, orderStatus,
                            rentDate, returnDate,
                            user.getUsername(), user.getNumberphone(),
                            currentRoom.getRoomID(), currentRoom.getNameroom(), currentRoom.getPriceroom(), currentRoom.getTyperoom(),
                            paymentMethod, totalprice);
                    mfirebase.saveOrder(order, new Firebase.SaveOrderCallback(){
                        @Override
                        public void onCallback(boolean isSuccess) {
                            if(isSuccess) {
                                mfirebase.updateStatusRoom(currentRoom.getRoomID(), "Đã Thuê", new Firebase.UpdateStatusOrderCallback() {
                                    @Override
                                    public void onCallback(boolean isSuccess) {
                                        if (isSuccess){
                                            showSuccessDialog();
                                        }
                                        else {
                                            Toast.makeText(OrderingActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(OrderingActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void Anhxa() {
        tv_nameroom = findViewById(R.id.tv_nameroom);
        tv_rentdate = findViewById(R.id.tv_rentdate);
        tv_returndate = findViewById(R.id.tv_returndate);
        tv_typeroom = findViewById(R.id.tv_typeroom);
        tv_priceroom = findViewById(R.id.tv_priceroom);
        tv_totalprice = findViewById(R.id.tv_totalprice);
        tv_username = findViewById(R.id.tv_username);
        tv_numberphone = findViewById(R.id.tv_numberphone);
        btn_rentdate = findViewById(R.id.btn_rentdate);
        btn_returndate = findViewById(R.id.btn_returndate);
        btn_comfirm = findViewById(R.id.btn_comfirm);
        rgPaymentMethor = findViewById(R.id.rg_ppthanhtoan);
        toolbar = findViewById(R.id.toolbarordering);
        userID = Loginstatus.getInstance().getUserID();
        mfirebase = new Firebase(this);
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
        tv_nameroom.setText(currentRoom.getNameroom());
        tv_priceroom.setText(String.format("%,d", Math.round(currentRoom.getPriceroom())) + " VNĐ/Ngày");
        tv_typeroom.setText(currentRoom.getTyperoom());
        currentDate = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
        curdate = LocalDate.parse(currentDate, formatter);
        rentDate = currentDate;
        tv_rentdate.setText(currentDate);
        tv_returndate.setText(currentDate);
        tv_totalprice.setText("");
        tv_numberphone.setText(user.getNumberphone());
        if(user.getUsername() != null) tv_username.setText(user.getUsername());
    }
    private void showdialogrentdate(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog rentDatePickerDialog = new DatePickerDialog(OrderingActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        rentDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
                        LocalDate date3 = LocalDate.parse(rentDate, formatter);
                        if (curdate.isAfter(date3)){
                            Toast.makeText(OrderingActivity.this, "Ngày thuê không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        tv_rentdate.setText(rentDate);
                        if (rentDate != null && returnDate != null){
                            settotalprice();
                        }
                    }
                }, mYear, mMonth, mDay);
        rentDatePickerDialog.setTitle("Chọn ngày thuê");
        rentDatePickerDialog.show();
    }
    private void showdialogreturndate(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog returnDatePickerDialog = new DatePickerDialog(OrderingActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        returnDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        if (rentDate != null && returnDate != null){
                            settotalprice();
                        }
                    }
                }, mYear, mMonth, mDay);
        returnDatePickerDialog.setTitle("Chọn ngày trả");
        returnDatePickerDialog.show();
    }
    private void settotalprice(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
        LocalDate date1 = LocalDate.parse(rentDate, formatter);
        LocalDate date2 = LocalDate.parse(returnDate, formatter);
        if (date2.isAfter(date1)) {
            tv_returndate.setText(returnDate);
            long songaythue = ChronoUnit.DAYS.between(date1, date2);
            totalprice = currentRoom.getPriceroom() * songaythue;
            tv_totalprice.setText(String.format("%,d", Math.round(totalprice)) + " VNĐ");
        } else {
            Toast.makeText(this, "Ngày thuê không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
    private void showSuccessDialog(){
        ConstraintLayout constraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(OrderingActivity.this).inflate(R.layout.dialog_success, constraintLayout);
        Button btn_done = view.findViewById(R.id.btn_success);

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderingActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                onBackPressed();
                finish();
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}