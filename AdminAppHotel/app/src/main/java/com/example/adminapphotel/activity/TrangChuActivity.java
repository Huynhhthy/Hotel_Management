package com.example.adminapphotel.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminapphotel.R;
import com.example.adminapphotel.fragment.AccountFragment;
import com.example.adminapphotel.fragment.AllOrdersFragment;
import com.example.adminapphotel.fragment.AllRoomsFragment;
import com.example.adminapphotel.fragment.AllUsersFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class TrangChuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_ALLORDERS = 0;
    private static final int FRAGMENT_ALLUSERS = 1;
    private static final int FRAGMENT_ALLROOMS = 2;
    private static final int FRAGMENT_ACCOUNT = 3;
    private int mCurrentFragment = FRAGMENT_ALLORDERS;
    private DrawerLayout mdrawerLayout;
    private NavigationView mnavigationView;
    private Toolbar mtoolbar;
    private ImageView mimg_avatar;
    private TextView mtv_nameadmin;
    private String adminID;
    private FirebaseUser mfirebaseUser;
    private FirebaseFirestore mfirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        Anhxa();
        getUserData();
        ActionBar();
        replaceFragment(new AllOrdersFragment());
        mnavigationView.getMenu().findItem(R.id.item_allOrder).setChecked(true);
    }

    private void Anhxa() {
        mdrawerLayout = findViewById(R.id.drawer_trangchu);
        mnavigationView = findViewById(R.id.nav_trangchu);
        mtoolbar = findViewById(R.id.toolbar_trangchu);
        mimg_avatar = mnavigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        mtv_nameadmin = mnavigationView.getHeaderView(0).findViewById(R.id.tv_nameadmin);
        mfirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mfirestore = FirebaseFirestore.getInstance();
        mnavigationView.setNavigationItemSelectedListener(this);
    }
    private void ActionBar(){
        setSupportActionBar(mtoolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, // context (thường là Activity chứa drawer)
                mdrawerLayout, // tham chiếu tới DrawerLayout
                mtoolbar, // tham chiếu tới Toolbar (nếu bạn dùng Action Bar, thay thế bằng R.string.drawer_open, R.string.drawer_close)
                R.string.navigation_drawer_open, // chuỗi mô tả hành động mở drawer
                R.string.navigation_drawer_close // chuỗi mô tả hành động đóng drawer
        );

        mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_allOrder){
            if (mCurrentFragment != FRAGMENT_ALLORDERS){
                if (mnavigationView.getMenu().findItem(R.id.item_allOrder).isChecked() != true){
                    mnavigationView.getMenu().findItem(R.id.item_allOrder).setChecked(true);
                    mnavigationView.getMenu().findItem(R.id.item_allUser).setChecked(false);
                    mnavigationView.getMenu().findItem(R.id.item_allRoom).setChecked(false);
                    mnavigationView.getMenu().findItem(R.id.item_account).setChecked(false);
                }
                replaceFragment(new AllOrdersFragment());
                mtoolbar.setTitle("All Orders");
                mCurrentFragment = FRAGMENT_ALLORDERS;
            }
        }else if(id == R.id.item_allUser){
            if (mCurrentFragment != FRAGMENT_ALLUSERS){
                if (mnavigationView.getMenu().findItem(R.id.item_allUser).isChecked() != true){
                    mnavigationView.getMenu().findItem(R.id.item_allOrder).setChecked(false);
                    mnavigationView.getMenu().findItem(R.id.item_allUser).setChecked(true);
                    mnavigationView.getMenu().findItem(R.id.item_allRoom).setChecked(false);
                    mnavigationView.getMenu().findItem(R.id.item_account).setChecked(false);
                }
                replaceFragment(new AllUsersFragment());
                mtoolbar.setTitle("All Users");
                mCurrentFragment = FRAGMENT_ALLUSERS;
            }
        }else if(id == R.id.item_allRoom){
            if (mCurrentFragment != FRAGMENT_ALLROOMS){
                if (mnavigationView.getMenu().findItem(R.id.item_allRoom).isChecked() != true){
                    mnavigationView.getMenu().findItem(R.id.item_allOrder).setChecked(false);
                    mnavigationView.getMenu().findItem(R.id.item_allUser).setChecked(false);
                    mnavigationView.getMenu().findItem(R.id.item_allRoom).setChecked(true);
                    mnavigationView.getMenu().findItem(R.id.item_account).setChecked(false);
                }
                replaceFragment(new AllRoomsFragment());
                mtoolbar.setTitle("All Cars");
                mCurrentFragment = FRAGMENT_ALLROOMS;
            }
        }else if(id == R.id.item_account){
            if (mCurrentFragment != FRAGMENT_ACCOUNT){
                if (mnavigationView.getMenu().findItem(R.id.item_account).isChecked() != true){
                    mnavigationView.getMenu().findItem(R.id.item_allOrder).setChecked(false);
                    mnavigationView.getMenu().findItem(R.id.item_allUser).setChecked(false);
                    mnavigationView.getMenu().findItem(R.id.item_allRoom).setChecked(false);
                    mnavigationView.getMenu().findItem(R.id.item_account).setChecked(true);
                }
                replaceFragment(new AccountFragment(adminID));
                mtoolbar.setTitle("Account");
                mCurrentFragment = FRAGMENT_ACCOUNT;
            }
        }else if(id == R.id.item_signout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(TrangChuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        mdrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
    @Override
    public void onBackPressed() {
        if(mdrawerLayout.isDrawerOpen(GravityCompat.START)){
            mdrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_fragment, fragment);
        transaction.commit();
    }
    private void getUserData() {
        Intent i = getIntent();
        adminID = i.getStringExtra("adminID");
    }
}