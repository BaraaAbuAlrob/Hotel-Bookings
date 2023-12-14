package com.finalandroid.project.hotelbooking.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.finalandroid.project.hotelbooking.Fragments.FragmentHotelsOwner;
import com.finalandroid.project.hotelbooking.Fragments.FragmentNotifications;
import com.finalandroid.project.hotelbooking.Fragments.FragmentNotificationsOwner;
import com.finalandroid.project.hotelbooking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityOwner extends AppCompatActivity {

    FrameLayout frameLayout;
    FragmentTransaction mFragmentTransaction;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_owner);

        frameLayout = findViewById(R.id.MA_Owner_frame_layout);
        bottomNavigationView = findViewById(R.id.MA_Owner_bottom_nav);


        // Call the FragmentHotelsOwner using FragmentTransaction
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.MA_Owner_frame_layout, new FragmentHotelsOwner());
        mFragmentTransaction.commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.bot_hotels) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.MA_Owner_frame_layout, new FragmentHotelsOwner());
                mFragmentTransaction.commit();

            } else if(item.getItemId() == R.id.bot_notifications){
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.MA_Owner_frame_layout, new FragmentNotificationsOwner());
                mFragmentTransaction.commit();

            } else if(item.getItemId() == R.id.bot_profile){
                startActivity(new Intent(this, HotelOwnerProfile.class));
            }

            return true;
        });

    } //End onCreate(...) method

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        int notification = intent.getIntExtra("noti", 0);

        if(notification == 777){
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.replace(R.id.frame_layout, new FragmentNotifications());
            mFragmentTransaction.commit();

        }
    }
}