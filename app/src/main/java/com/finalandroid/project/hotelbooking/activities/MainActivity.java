package com.finalandroid.project.hotelbooking.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.finalandroid.project.hotelbooking.Fragments.FragmentHotels;
import com.finalandroid.project.hotelbooking.Fragments.FragmentNotifications;
import com.finalandroid.project.hotelbooking.Fragments.FragmentSettings;
import com.finalandroid.project.hotelbooking.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    FrameLayout frameLayout;
    FragmentTransaction mFragmentTransaction;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frame_layout);
        bottomNavigationView = findViewById(R.id.bottom_nav);

//        bottomNavigationView.setBackground(null);

        // Call the FragmentHotels using FragmentTransaction
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.frame_layout, new FragmentHotels());
        mFragmentTransaction.commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.bot_hotels) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.frame_layout, new FragmentHotels());
                mFragmentTransaction.commit();

            } else if(item.getItemId() == R.id.bot_notifications){
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.frame_layout, new FragmentNotifications());
                mFragmentTransaction.commit();

            } else if(item.getItemId() == R.id.bot_settings){
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.frame_layout, new FragmentSettings());
                mFragmentTransaction.commit();
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