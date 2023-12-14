package com.finalandroid.project.hotelbooking.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.adapters.HotelOwnerProfile_HotelsAdapter;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.Hotel;
import com.finalandroid.project.hotelbooking.modelClass.HotelResult;
import com.finalandroid.project.hotelbooking.modelClass.Result;
import com.finalandroid.project.hotelbooking.modelClass.User;
import com.finalandroid.project.hotelbooking.modelClass.UserResult;
import com.finalandroid.project.hotelbooking.sharedPref.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelOwnerProfile extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE_PICK_1 = 145;
    private final int REQUEST_CODE_PICK_2 = 146;

    private ImageView ProfileImage, PassImage, HotelImage, AboutUs;
    private TextView PasswordText;
    private TextView noHotelYet;
    private TextInputLayout Name, Email, Phone, Password, HotelName, HotelLocation;
    private User user;
    private GridView gridView;
    private List<Hotel> hoteslList;
    private String SName, SEmail, SPhone, SPassword, hSName, hSLocation;
    private Button CreateHotel;
    private Button YourHotels;
    private Button UpdateInfo;
    private Button Logout;
    private Button DeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_owner_profile);

        ProfileImage = findViewById(R.id.hotelOwner_profile_ur_photo);
        PassImage = findViewById(R.id.hotelOwner_profile_pass_eye);
        PasswordText = findViewById(R.id.hotelOwner_profile_pass_text);
        CreateHotel = findViewById(R.id.hotelOwner_createNewHotel);
        YourHotels = findViewById(R.id.hotelOwner_yourHotels);
        noHotelYet = findViewById(R.id.hotelOwner_noHY);
        UpdateInfo = findViewById(R.id.hotelOwner_edit_ur_info);
        Logout = findViewById(R.id.hotelOwner_logout);
        AboutUs = findViewById(R.id.hotelOwner_aboutUs);
        DeleteAccount = findViewById(R.id.hotelOwner_del_ur_account);

        TextView username = findViewById(R.id.hotelOwner_profile_ur_name);
        TextView email = findViewById(R.id.hotelOwner_profile_ur_email);
        TextView Phone = findViewById(R.id.hotelOwner_profile_ur_phone);

        user = SharedPrefManager.getInstance(this).getUser();
        username.setText(user.getName());
        email.setText(user.getEmail());
        Phone.setText(user.getPhone());
        PasswordText.setText(user.getPassword());

        checkCreatedHotels();

        ProfileImage.setOnClickListener(this);
        PassImage.setOnClickListener(this);
        PasswordText.setOnClickListener(this);
        CreateHotel.setOnClickListener(this);
        YourHotels.setOnClickListener(this);
        UpdateInfo.setOnClickListener(this);
        Logout.setOnClickListener(this);
        AboutUs.setOnClickListener(this);
        DeleteAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == ProfileImage){

            View view = LayoutInflater.from(HotelOwnerProfile.this).inflate(R.layout.custom_dialog_update_image, null, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(HotelOwnerProfile.this);
            builder.setView(view);
            Dialog dialog = builder.create();
            dialog.show();

            Button TakePhoto = view.findViewById(R.id.btn_take_photo);
            Button LoadPhoto = view.findViewById(R.id.btn_load_photo);

            TakePhoto.setOnClickListener(v1 -> {

                takePhoto();
                dialog.cancel();
            });
            LoadPhoto.setOnClickListener(v12 -> {

                loadPhoto(REQUEST_CODE_PICK_1);
                dialog.cancel();
            });

        } else if (v == PassImage) {
            PassImage.setVisibility(View.GONE);
            PasswordText.setVisibility(View.VISIBLE);

        } else if (v == PasswordText) {
            PasswordText.setVisibility(View.GONE);
            PassImage.setVisibility(View.VISIBLE);

        } else if (v == CreateHotel) {

            View view = LayoutInflater.from(HotelOwnerProfile.this).inflate(R.layout.custom_dialog_create_new_hotel, null, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(HotelOwnerProfile.this);
            builder.setView(view);
            Dialog dialog = builder.create();
            dialog.show();

            HotelImage = view.findViewById(R.id.hotel_dialog_user_img);
            HotelName = view.findViewById(R.id.hotel_dialog_hotelName);
            HotelLocation = view.findViewById(R.id.hotel_dialog_hotelLocation);
            Button choseFile = view.findViewById(R.id.hotel_dialog_chose_file);
            Button create = view.findViewById(R.id.hotel_dialog_create);
            Button cancel = view.findViewById(R.id.hotel_dialog_cancel);

            choseFile.setOnClickListener(v13 -> loadPhoto(REQUEST_CODE_PICK_2));

            create.setOnClickListener(v14 -> {

                if(!hotelNameValidate() || !hotelLocationValidate()){
                    Log.e("Validate:", "Invalid");

                } else {
                    //Call updateUser method
                    CreateHotel();
                    dialog.cancel();
                }
            });

            cancel.setOnClickListener(v15 -> dialog.cancel());

        } else if (v == YourHotels) {

            View view = LayoutInflater.from(HotelOwnerProfile.this).inflate(R.layout.custom_dialog_your_hotels, null, false);
            gridView = view.findViewById(R.id.hotelOwner_gridview_hotels);
            Button close = view.findViewById(R.id.c_d_y_h_close);

            AlertDialog.Builder builder = new AlertDialog.Builder(HotelOwnerProfile.this);
            builder.setView(view);
            Dialog dialog = builder.create();
            dialog.show();

            loadCreatedHotels();

            gridView.setOnItemClickListener((parent, view1, position, id) -> {

                Intent intent = new Intent(HotelOwnerProfile.this, HotelRooms.class);
                intent.putExtra("hotelId", hoteslList.get(position).getId());
                intent.putExtra("hotelName", hoteslList.get(position).getName());
                startActivity(intent);
                dialog.cancel();
            });

            close.setOnClickListener(v1 -> dialog.cancel());

        } else if (v == UpdateInfo){

            View view = LayoutInflater.from(HotelOwnerProfile.this).inflate(R.layout.custom_dialog_update_info, null, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(HotelOwnerProfile.this);
            builder.setView(view);
            Dialog dialog = builder.create();
            dialog.show();

            Name = view.findViewById(R.id.dia_usernameInputLayout);
            Email = view.findViewById(R.id.dia_emailInputLayout);
            Phone = view.findViewById(R.id.dia_phoneInputLayout);
            Password = view.findViewById(R.id.dia_passwordInputLayout);

            Objects.requireNonNull(Name.getEditText()).setText(user.getName());
            Objects.requireNonNull(Email.getEditText()).setText(user.getEmail());
            Objects.requireNonNull(Phone.getEditText()).setText(user.getPhone());
            Objects.requireNonNull(Password.getEditText()).setText(user.getPassword());

            Button edit = view.findViewById(R.id.dia_edit);
            Button cancel = view.findViewById(R.id.dia_cancel);

            edit.setOnClickListener(v1 -> {

                if(!usernameValidate() || !emailValidate() || !phoneValidate() || !passwordValidate()){
                    Log.e("Validate:", "Invalid");

                } else {
                    //Call updateUser method
                    updateUser();
                    dialog.cancel();
                    recreate();
                }
            });

            cancel.setOnClickListener(v12 -> dialog.cancel());

        } else if (v == Logout) {
            new AlertDialog.Builder(HotelOwnerProfile.this)
                    .setTitle(R.string.logout)
                    .setMessage(R.string.areYouSure)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {

                        finish();
                        SharedPrefManager.getInstance(HotelOwnerProfile.this).logout();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .create()
                    .show();

        } else if (v == AboutUs) {

            new AlertDialog.Builder(HotelOwnerProfile.this)
                    .setIcon(R.drawable.ic_info)
                    .setTitle(R.string.about_Us)
                    .setMessage(R.string.about_Us_message)
                    .setNegativeButton(R.string.back, (dialog, which) -> dialog.cancel())
                    .create()
                    .show();

        } else if (v == DeleteAccount) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning)
                    .setTitle(R.string.note)
                    .setMessage(R.string.youWillLossYourAcc)
                    .setPositiveButton(R.string.delete, (dialog, which) -> {

                        int userId = user.getId();
                        Call<Result> call = RestApiConnection.getInstance().getMyApi().deleteAccount(userId);

                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                                assert response.body() != null;
                                if (!response.body().getError()) {

                                    finish();
                                    SharedPrefManager.getInstance(HotelOwnerProfile.this).delAcc();
                                    dialog.cancel();
                                }

                                Toast.makeText(HotelOwnerProfile.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                                Log.e("onFailure", Objects.requireNonNull(t.getMessage()));
                                Toast.makeText(HotelOwnerProfile.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .create()
                    .show();
        }
    }

    private void CreateHotel(){


        Call<HotelResult> call = RestApiConnection.getInstance().getMyApi()
                .createNewHotel(user.getId(), hSName, "deluxe_delhi.jpeg", hSLocation);

        call.enqueue(new Callback<HotelResult>() {
            @Override
            public void onResponse(@NonNull Call<HotelResult> call, @NonNull Response<HotelResult> response) {

                assert response.body() != null;
                Toast.makeText(HotelOwnerProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<HotelResult> call, @NonNull Throwable t) {

                Toast.makeText(HotelOwnerProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCreatedHotels() {

        Call<List<Hotel>> call = RestApiConnection.getInstance().getMyApi().getCreatedHotels(user.getId());

        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Hotel>> call, @NonNull Response<List<Hotel>> response) {

                hoteslList = response.body();

                assert hoteslList != null;
                if (!hoteslList.get(0).getImg().equalsIgnoreCase("No Data")) {
                    noHotelYet.setVisibility(View.GONE);
                    YourHotels.setVisibility(View.VISIBLE);

                    HotelOwnerProfile_HotelsAdapter adapter =
                            new HotelOwnerProfile_HotelsAdapter(HotelOwnerProfile.this, hoteslList);
                    gridView.setAdapter(adapter);

                } else {
                    YourHotels.setVisibility(View.GONE);
                    noHotelYet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Hotel>> call, @NonNull Throwable t) {

                Toast.makeText(HotelOwnerProfile.this, "onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkCreatedHotels() {

        Call<List<Hotel>> call = RestApiConnection.getInstance().getMyApi().getCreatedHotels(user.getId());

        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Hotel>> call, @NonNull Response<List<Hotel>> response) {

                hoteslList = response.body();

                assert hoteslList != null;
                if (!hoteslList.get(0).getImg().equalsIgnoreCase("No Data")) {
                    noHotelYet.setVisibility(View.GONE);
                    YourHotels.setVisibility(View.VISIBLE);

                } else {
                    YourHotels.setVisibility(View.GONE);
                    noHotelYet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Hotel>> call, @NonNull Throwable t) {

                Toast.makeText(HotelOwnerProfile.this, "onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser() {

        int id = user.getId();
        Call<UserResult> call = RestApiConnection.getInstance().getMyApi().updateUserInfo(id, SName, SEmail, SPhone, SPassword);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(@NonNull Call<UserResult> call, @NonNull Response<UserResult> response) {

                assert response.body() != null;
                if(!response.body().getError()) {
                    User u = response.body().getUser();
                    u.setUserTypes("hotel owner");

                    SharedPrefManager.getInstance(HotelOwnerProfile.this).userUpdate(u);
                    recreate();
                }

                Log.e("onResponse: ", response.body().getMessage());
                Toast.makeText(HotelOwnerProfile.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Call<UserResult> call, @NonNull Throwable t) {

                Log.e("onFailure: ", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(HotelOwnerProfile.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    } // End of updateUser() method.

    private void takePhoto() {

        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            try{
                Intent cameraIntent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mStartForResult.launch(cameraIntent);

            } catch (Exception e){
                Toast.makeText(getApplicationContext(), "Camera is not available (it use or does not exist).", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Hasn't a camera :(", Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        Bitmap photo = (Bitmap) Objects.requireNonNull(intent.getExtras()).get("data");
                        ProfileImage.setImageBitmap(photo);
                    }
                }
            });// End of the object declaration.

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
            Picasso.with(HotelOwnerProfile.this).load(mImageUri).into(ProfileImage); ///////    Picasso   \\\\\\\\

        } else if (requestCode == REQUEST_CODE_PICK_2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mImageUri = data.getData();
            Picasso.with(HotelOwnerProfile.this).load(mImageUri).into(HotelImage);

        } else {
            Toast.makeText(this, "Something goes wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean usernameValidate(){

        // ضفت ال ()getEditText لأن ال TextField من ال material design ولانه داخل ال TextInputLayout
        SName = Objects.requireNonNull(Name.getEditText()).getText().toString().trim();

        if(SName.isEmpty()){
            Name.setError("Required field!. This field should not be empty!");
            return false;

        } else if(SName.length() > 300){
            Name.setError("Username field should not be grater than 300 character");
            return false;

        } else {
            Name.setErrorEnabled(false);
            return true;
        }
    }// End of usernameValidate method

    private boolean passwordValidate(){
        SPassword = Objects.requireNonNull(Password.getEditText()).getText().toString().trim();

        if(SPassword.isEmpty()){
            Password.setError("Required field!. This field should not be empty!");
            return false;

        } else if(SPassword.length() != 8){
            Password.setError("Password field should be 8 characters!");
            return false;

        } else {
            Password.setErrorEnabled(false);
            return true;
        }
    }// End of passwordValidate method

    private boolean phoneValidate(){

        // the trim() method delete the spaces if founded.
        SPhone = Objects.requireNonNull(Phone.getEditText()).getText().toString().trim();


//          To save the country/international code for check it.
//        validateIDs = phone.substring(0, 3);

        if (SPhone.isEmpty()) {
            Phone.setError("Required field!. This field should not be empty!");
            return false;

        } else if (SPhone.length() != 10) {
            Phone.setError("The length of number should be 10 digits!");
            return false;

        } else if (!SPhone.startsWith("056") && !SPhone.startsWith("059")) {

            Phone.setError("Invalid international code!");
            return false;

        } else {
            Phone.setErrorEnabled(false);
            return true;
        }
    }// End of phoneValidate method

    private boolean emailValidate(){
        SEmail = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();

        if(SEmail.isEmpty()){
            Email.setError("Required field!. This field should not be empty!");
            return false;

//             Way #1: Using Regular Expression

//         } else if (!Pattern.matches("[a-z]+[[a-z0-9][a-z]]+@gmail.com", email)){
//             Email.setError("Invalid Email!");
//             return false;
//
//         }

//             Way #2:
        } else if (!Patterns.EMAIL_ADDRESS.matcher(SEmail).matches()){
            Email.setError("Invalid Email!");
            return false;

        } else {
            Email.setErrorEnabled(false);
            return true;
        }
    }// End of emailValidate method

    private boolean hotelNameValidate(){

        // ضفت ال ()getEditText لأن ال TextField من ال material design ولانه داخل ال TextInputLayout
        hSName = Objects.requireNonNull(HotelName.getEditText()).getText().toString().trim();

        if(hSName.isEmpty()){
            HotelName.setError("Required field!. This field should not be empty!");
            return false;

        } else if(hSName.length() > 300){
            HotelName.setError("Username field should not be grater than 300 character.");
            return false;

        } else {
            HotelName.setErrorEnabled(false);
            return true;
        }
    }// End of usernameValidate method

    private boolean hotelLocationValidate(){

        // ضفت ال ()getEditText لأن ال TextField من ال material design ولانه داخل ال TextInputLayout
        hSLocation = Objects.requireNonNull(HotelLocation.getEditText()).getText().toString().trim();

        if(hSLocation.isEmpty()){
            HotelLocation.setError("Required field!. This field should not be empty!");
            return false;

        } else if(hSLocation.length() > 300){
            HotelLocation.setError("Username field should not be grater than 300 character");
            return false;

        } else {
            HotelLocation.setErrorEnabled(false);
            return true;
        }
    }// End of usernameValidate method
}