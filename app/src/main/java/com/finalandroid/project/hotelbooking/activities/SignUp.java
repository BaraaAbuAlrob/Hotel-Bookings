package com.finalandroid.project.hotelbooking.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.UserResult;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private final int REQUEST_CODE_PICK = 99;
    Uri mImageUri;
    private ImageView User_img;
//    private ProgressBar ProgressBar;


    private TextInputLayout Username, Email, Phone, Password;
    private String username, email, phone, password, image, userTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // flat the components:
        User_img = findViewById(R.id.signUp_user_img);
        Button choseFile = findViewById(R.id.signUp_chose_file);
//        ProgressBar = findViewById(R.id.signUp_progress_bar);

        Username = findViewById(R.id.usernameInputLayout);
        Password = findViewById(R.id.passwordInputLayout);
        Phone = findViewById(R.id.phoneInputLayout);
        Email = findViewById(R.id.emailInputLayout);
        Spinner UserTypes = findViewById(R.id.signUp_user_type);

        Button signUp = findViewById(R.id.signupBtn);

        choseFile.setOnClickListener(v -> openFileChooser());

        signUp.setOnClickListener(view -> {

            if(!usernameValidate() || !emailValidate() || !phoneValidate() || !passwordValidate()) {}
            else
                //Call insertUser method
                insertUser();
        });

        UserTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        userTypes = "tenant";
                        break;

                    case 1:
                        userTypes = "hotel owner";
                        break;

                    default:
                        Toast.makeText(SignUp.this, "Unexpected value: " + position, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }// End of onCreate method

    private void openFileChooser() {

        Intent intent = new Intent();
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null){

            mImageUri = data.getData();

            if(mImageUri != null)
                image = mImageUri.toString();
            else
                image = "";

            Picasso.with(SignUp.this).load(mImageUri).into(User_img); ///////    Picasso   \\\\\\\\
        }
    }

    private void insertUser(){

        username = Objects.requireNonNull(Username.getEditText()).getText().toString().trim();
        email = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();
        phone = Objects.requireNonNull(Phone.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(Password.getEditText()).getText().toString().trim();
//        image = Objects.requireNonNull(Image.getEditText()).getText().toString().trim();

        Call<UserResult> call = RestApiConnection.getInstance().getMyApi().insertUser(username, email, phone, password, userTypes);

        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(@NonNull Call<UserResult> call, @NonNull Response<UserResult> response) {

                assert response.body() != null;
                Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                if (!response.body().getError()) {

                    finish();
                    startActivity(new Intent(SignUp.this, SignIn.class));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResult> call, @NonNull Throwable t) {

                Log.i("Failed to insert data:", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(SignUp.this, "Failed to insert data: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }//End insertUser method

    private boolean usernameValidate(){

        // ضفت ال ()getEditText لأن ال TextField من ال material design ولانه داخل ال TextInputLayout
        username = Objects.requireNonNull(Username.getEditText()).getText().toString().trim();

        if(username.isEmpty()){
            Username.setError("Required field!. This field should not be empty!");
            return false;

        } else if(username.length() > 300){
            Username.setError("Username field should not be grater than 300 character");
            return false;

        } else {
            Username.setErrorEnabled(false);
            return true;
        }
    }// End of usernameValidate method

    private boolean passwordValidate(){
        password = Objects.requireNonNull(Password.getEditText()).getText().toString().trim();

        if(password.isEmpty()){
            Password.setError("Required field!. This field should not be empty!");
            return false;

        } else if(password.length() != 8){
            Password.setError("Password field should be 8 characters!");
            return false;

        } else {
            Password.setErrorEnabled(false);
            return true;
        }
    }// End of passwordValidate method

    private boolean phoneValidate(){

        // the trim() method delete the spaces if founded.
        phone = Objects.requireNonNull(Phone.getEditText()).getText().toString().trim();


//          To save the country/international code for check it.
//        validateIDs = phone.substring(0, 3);

        if (phone.isEmpty()) {
            Phone.setError("Required field!. This field should not be empty!");
            return false;

        } else if (phone.length() != 10) {
            Phone.setError("The length of number should be 10 digits!");
            return false;

        } else if (!phone.startsWith("056") && !phone.startsWith("059")) {

            Phone.setError("Invalid international code!");
            return false;

        } else {
            Phone.setErrorEnabled(false);
            return true;
        }
    }// End of phoneValidate method

    private boolean emailValidate(){
        email = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();

        if(email.isEmpty()){
            Email.setError("Required field!. This field should not be empty!");
            return false;

//             Way #1: Using Regular Expression

//         } else if (!Pattern.matches("[a-z]+[[a-z0-9][a-z]]+@gmail.com", email)){
//             Email.setError("Invalid Email!");
//             return false;
//
//         }

//             Way #2:
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Invalid Email!");
            return false;

        } else {
            Email.setErrorEnabled(false);
            return true;
        }
    }// End of emailValidate method
}// End of MainActivity Class.