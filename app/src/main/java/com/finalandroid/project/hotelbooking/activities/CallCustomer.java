package com.finalandroid.project.hotelbooking.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.finalandroid.project.hotelbooking.R;

public class CallCustomer extends AppCompatActivity {

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_customer);

        Button call = findViewById(R.id.CC_call);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");

        callTheCustomer();
        call.setOnClickListener(v -> callTheCustomer());
    }

    private void callTheCustomer() {

        //first we have to check permission for make this phone call
        if (ContextCompat.checkSelfPermission(CallCustomer.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            // If permission was denied here we need to request new permission.
            ActivityCompat.requestPermissions(CallCustomer.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
            try {
                startCallPhone();
                Toast.makeText(this, "Calling...", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

                Toast.makeText(this, "Permission accepted", Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                startCallPhone();
                Toast.makeText(this, "Calling...", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(this, "Sorry, some thing goes wrong...!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCallPhone() {

        String phoneNumber = "tel:" + phone;

        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
        startActivity(callIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}