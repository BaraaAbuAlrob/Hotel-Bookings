package com.finalandroid.project.hotelbooking.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.UserResult;
import com.finalandroid.project.hotelbooking.modelClass.User;
import com.finalandroid.project.hotelbooking.sharedPref.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {

    TextInputLayout Phone, Password;
    Button signIn, goToSignUp;
    String phone, password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        chickLoggedIN();

        Phone = findViewById(R.id.TextInputPhone);
        Password = findViewById(R.id.TextInputPassword);

        signIn = findViewById(R.id.signInBtn);
        goToSignUp = findViewById(R.id.signUpActivity);

        goToSignUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUp.class)));

        signIn.setOnClickListener(view -> {

            if(phoneValidate() && passwordValidate())
                userSignIn();

        });
    } // End onCreate() method.

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void chickLoggedIN() {

        if (SharedPrefManager.getInstance(SignIn.this).isLoggedIN()) {
            finish();
            String userType = SharedPrefManager.getInstance(SignIn.this).getUser().getUserTypes();
            switch (userType){

                case "tenant":
                    startActivity(new Intent(SignIn.this, MainActivity.class));
                    break;

                case "hotel owner":
                    startActivity(new Intent(SignIn.this, MainActivityOwner.class));
                    break;

                default:
                    Toast.makeText(this, "User type undefined!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void userSignIn(){

        /**
         استرجاع البيانات الني نم إدخالها من ال EditText Views
         */
        String phone = Objects.requireNonNull(Phone.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(Password.getEditText()).getText().toString().trim();

        /**
         استدعاء ال RestApiConnection class والحصول على ال instance ومن ثم لل APIServices obj
         وأخيرا الوصول للميثود userLogin(phone, password) يلي رح تستقبل الداتا يلي ادخلها المستخدم وترسلها ل
         ملف ال php والذي يمثل ال API وهو بإسم "userSignIn.php"
         */

        Call<UserResult> call = RestApiConnection.getInstance().getMyApi().userLogin(phone, password);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(@NonNull Call<UserResult> call, @NonNull Response<UserResult> response) {

                assert response.body() != null;
                if (!response.body().getError()) {

//                    Create a User object and initialized it with it's info
                    User user = new User(
                            response.body().getUser().getId(),
                            response.body().getUser().getName(),
                            response.body().getUser().getEmail(),
                            response.body().getUser().getPhone(),
                            response.body().getUser().getPassword(),
                            response.body().getUser().getUserTypes());
//                    User user = response.body().getUser(); // true

                    finish();
                    switch (user.getUserTypes()){
                        case "tenant":
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                            break;

                        case "hotel owner":
                            startActivity(new Intent(SignIn.this, MainActivityOwner.class));
                            break;
                    }
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                }

                Toast.makeText(SignIn.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Call<UserResult> call, @NonNull Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.i("Failed: ", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    boolean phoneValidate(){

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

    boolean passwordValidate(){
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
}
