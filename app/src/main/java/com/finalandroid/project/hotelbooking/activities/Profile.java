package com.finalandroid.project.hotelbooking.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.Result;
import com.finalandroid.project.hotelbooking.modelClass.User;
import com.finalandroid.project.hotelbooking.modelClass.UserResult;
import com.finalandroid.project.hotelbooking.sharedPref.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    TextView username,email,phone,passText;
    ImageView userPhoto, passImg;
    Button editInfo, deleteAccount;

    User user;
    TextInputLayout Name, Email, Phone, Password;
    String EName, EEmail, EPhone, EPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        userPhoto = findViewById(R.id.profile_ur_photo);

        username = findViewById(R.id.profile_ur_name);
        email = findViewById(R.id.profile_ur_email);
        phone = findViewById(R.id.profile_ur_phone);
        passText = findViewById(R.id.profile_pass_text);
        passImg = findViewById(R.id.profile_pass_eye);

        editInfo = findViewById(R.id.edit_ur_info);
        deleteAccount = findViewById(R.id.del_ur_account);

//        User Info
        user = SharedPrefManager.getInstance(this).getUser();

        /*

        Picasso.with(Profile.this).load(userUriImg).into(userPhoto);

        Glide.with(this)
                .load(RestApiConnection.PHOTO_BASE_URL + "users/" + user.getImg())
                .apply(new RequestOptions().override(110, 110))
                .error(R.drawable.notfound)
                .into(userPhoto);
         */

        username.setText(user.getName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        passText.setText(user.getPassword());

        passText.setOnClickListener(this);
        passImg.setOnClickListener(this);
        editInfo.setOnClickListener(this);
        deleteAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == passText){
            passText.setVisibility(View.GONE);
            passImg.setVisibility(View.VISIBLE);

        } else if (v == passImg) {
            passImg.setVisibility(View.GONE);
            passText.setVisibility(View.VISIBLE);

        } else if (v == editInfo) {

            View view = LayoutInflater.from(Profile.this).inflate(R.layout.custom_dialog_update_info, null, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
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
                }
            });

            cancel.setOnClickListener(v12 -> dialog.cancel());

        } else if (v == deleteAccount) {
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
                                    SharedPrefManager.getInstance(Profile.this).delAcc();
                                    dialog.cancel();
                                }

                                Toast.makeText(Profile.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                                Log.e("onFailure", Objects.requireNonNull(t.getMessage()));
                                Toast.makeText(Profile.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .show();
        }
    }

    private void updateUser() {

        int id = user.getId();
        Call<UserResult> call = RestApiConnection.getInstance().getMyApi().updateUserInfo(id, EName, EEmail, EPhone, EPassword);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(@NonNull Call<UserResult> call, @NonNull Response<UserResult> response) {

                assert response.body() != null;
                if(!response.body().getError()){

                    User u = response.body().getUser();
                    u.setUserTypes("tenant");
                    SharedPrefManager.getInstance(Profile.this).userUpdate(u);
                    recreate();
                }

                Log.e("onResponse: ", response.body().getMessage());
                Toast.makeText(Profile.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Call<UserResult> call, @NonNull Throwable t) {

                Log.e("onFailure: ", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(Profile.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    } // End of updateUser() method.

    private boolean usernameValidate(){

        // ضفت ال ()getEditText لأن ال TextField من ال material design ولانه داخل ال TextInputLayout
        EName = Objects.requireNonNull(Name.getEditText()).getText().toString().trim();

        if(EName.isEmpty()){
            Name.setError("Required field!. This field should not be empty!");
            return false;

        } else if(username.length() > 300){
            Name.setError("Username field should not be grater than 300 character");
            return false;

        } else {
            Name.setErrorEnabled(false);
            return true;
        }
    }// End of usernameValidate method

    private boolean passwordValidate(){
        EPassword = Objects.requireNonNull(Password.getEditText()).getText().toString().trim();

        if(EPassword.isEmpty()){
            Password.setError("Required field!. This field should not be empty!");
            return false;

        } else if(EPassword.length() != 8){
            Password.setError("Password field should be 8 characters!");
            return false;

        } else {
            Password.setErrorEnabled(false);
            return true;
        }
    }// End of passwordValidate method

    private boolean phoneValidate(){

        // the trim() method delete the spaces if founded.
        EPhone = Objects.requireNonNull(Phone.getEditText()).getText().toString().trim();


//          To save the country/international code for check it.
//        validateIDs = phone.substring(0, 3);

        if (EPhone.isEmpty()) {
            Phone.setError("Required field!. This field should not be empty!");
            return false;

        } else if (EPhone.length() != 10) {
            Phone.setError("The length of number should be 10 digits!");
            return false;

        } else if (!EPhone.startsWith("056") && !EPhone.startsWith("059")) {

            Phone.setError("Invalid international code!");
            return false;

        } else {
            Phone.setErrorEnabled(false);
            return true;
        }
    }// End of phoneValidate method

    private boolean emailValidate(){
        EEmail = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();

        if(EEmail.isEmpty()){
            Email.setError("Required field!. This field should not be empty!");
            return false;

//             Way #1: Using Regular Expression

//         } else if (!Pattern.matches("[a-z]+[[a-z0-9][a-z]]+@gmail.com", email)){
//             Email.setError("Invalid Email!");
//             return false;
//
//         }

//             Way #2:
        } else if (!Patterns.EMAIL_ADDRESS.matcher(EEmail).matches()){
            Email.setError("Invalid Email!");
            return false;

        } else {
            Email.setErrorEnabled(false);
            return true;
        }
    }// End of emailValidate method
}