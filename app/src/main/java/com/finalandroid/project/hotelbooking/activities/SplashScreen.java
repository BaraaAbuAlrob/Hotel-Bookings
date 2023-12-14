package com.finalandroid.project.hotelbooking.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.finalandroid.project.hotelbooking.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Toast.makeText(this, R.string.checkingYourInternetConnection, Toast.LENGTH_LONG).show();

        Thread thread = new Thread(() -> {

            try {
                Thread.sleep(2500);
                Intent runSplash = new Intent(getApplicationContext(), SignIn.class);
                startActivities(new Intent[]{runSplash});
                finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}