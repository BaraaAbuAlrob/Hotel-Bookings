package com.finalandroid.project.hotelbooking.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.RoomResult;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRoomInfo extends AppCompatActivity {


    private final int REQUEST_CODE_PICK_1 = 4367;
    Intent intent;
    private ImageView RoomImage;
    private TextInputLayout NumberOfDays, ThePriceFANight, Description;
    private String SNumberOfDays, SThePriceFANight, SDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_room_info);

        RoomImage = findViewById(R.id.updateRoomInfo_room_img);
        NumberOfDays = findViewById(R.id.updateRoomInfo_numDaysInputLayout);
        ThePriceFANight = findViewById(R.id.updateRoomInfo_roomPriceFNightInputLayout);
        Description = findViewById(R.id.updateRoomInfo_descriptionInputLayout);
        Button choseFile = findViewById(R.id.roomUpdate_chose_file);
        Button update = findViewById(R.id.updateRoomInfo_updateBtn);

        intent = getIntent();
        fillTexts();

        choseFile.setOnClickListener(v -> loadPhoto(REQUEST_CODE_PICK_1));
        update.setOnClickListener(v -> {

            if(!NumDaysValidate() || !PriceFANightValidate() || !descriptionValidate()) {}
            else
                updateRoomInfo();
        });
    }

    private void updateRoomInfo(){

        int roomId = intent.getIntExtra("roomId", 0);
        int hotelId = intent.getIntExtra("hotelId", 0);

        Call<RoomResult> call = RestApiConnection.getInstance().getMyApi()
                .updateRoomInfo(roomId, hotelId, "q4.jpg", SNumberOfDays, SThePriceFANight, SDescription);

        call.enqueue(new Callback<RoomResult>() {
            @Override
            public void onResponse(@NonNull Call<RoomResult> call, @NonNull Response<RoomResult> response) {

//                if (!response.body().getError())
//                    // If you need store room info inside a Room object.

                assert response.body() != null;
                Toast.makeText(UpdateRoomInfo.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<RoomResult> call, @NonNull Throwable t) {

                Toast.makeText(UpdateRoomInfo.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillTexts(){

        Objects.requireNonNull(NumberOfDays.getEditText()).setText(intent.getStringExtra("numDays"));
        Objects.requireNonNull(ThePriceFANight.getEditText())
                .setText(Objects.requireNonNull(intent.getStringExtra("priceNight")).substring(0, 3));
        Objects.requireNonNull(Description.getEditText()).setText(intent.getStringExtra("description"));
    }

    private void loadPhoto(int requestCode) {

        Intent intent = new Intent();
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mImageUri = data.getData();
            Picasso.with(UpdateRoomInfo.this).load(mImageUri).into(RoomImage); ///////    Picasso   \\\\\\\\

        } else {
            Toast.makeText(this, "Something goes wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean NumDaysValidate() {
        SNumberOfDays = Objects.requireNonNull(NumberOfDays.getEditText()).getText().toString().trim();

        if (SNumberOfDays.isEmpty()) {
            NumberOfDays.setError("Required field!. This field should not be empty!");
            return false;

        } else if (SNumberOfDays.length() > 2) {
            NumberOfDays.setError("Number of days field should be less than 3 characters");
            return false;

        } else {
            NumberOfDays.setErrorEnabled(false);
            return true;
        }
    }

    private boolean PriceFANightValidate(){
        SThePriceFANight = Objects.requireNonNull(ThePriceFANight.getEditText()).getText().toString().trim();

        if(SThePriceFANight.isEmpty()){
            ThePriceFANight.setError("Required field!. This field should not be empty!");
            return false;

        } else if(SThePriceFANight.length() > 3){
            ThePriceFANight.setError("The price fora a night field should be less than 4 characters");
            return false;

        } else {
            ThePriceFANight.setErrorEnabled(false);
            return true;
        }
    }

    private boolean descriptionValidate(){
        SDescription = Objects.requireNonNull(Description.getEditText()).getText().toString().trim();

        if(SDescription.isEmpty()){
            Description.setError("Required field!. This field should not be empty!");
            return false;

        } else if(SDescription.length() > 500){
            Description.setError("The description field should be 500 characters or less");
            return false;

        } else {
            Description.setErrorEnabled(false);
            return true;
        }
    }// End of usernameValidate method
}