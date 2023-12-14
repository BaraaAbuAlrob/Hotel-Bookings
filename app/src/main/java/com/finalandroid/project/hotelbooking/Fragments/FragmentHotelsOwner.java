package com.finalandroid.project.hotelbooking.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.activities.HotelRooms;
import com.finalandroid.project.hotelbooking.adapters.HotelAdapterOwner;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.Hotel;
import com.finalandroid.project.hotelbooking.modelClass.HotelResult;
import com.finalandroid.project.hotelbooking.modelClass.User;
import com.finalandroid.project.hotelbooking.sharedPref.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHotelsOwner extends Fragment {

    private RecyclerView recyclerView;
    private TextView noHotelYet;
    private List<Hotel> hoteslList;
    private User user;
    private ImageView HotelImage;
    private TextInputLayout HotelName, HotelLocation;
    private Button ChoseFile;
    private String hSName, hSLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragments_hotels_owner, container, false); // True
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewHotelsOwner);
        noHotelYet = view.findViewById(R.id.text_noData_yetOwner);
        FloatingActionButton fab = view.findViewById(R.id.createHotelOwner);

        user = SharedPrefManager.getInstance(requireContext()).getUser();

        fab.setOnClickListener(v -> {

            View inflateView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog_create_new_hotel, null, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(inflateView);
            Dialog dialog = builder.create();
            dialog.show();

            HotelImage = inflateView.findViewById(R.id.hotel_dialog_user_img);
            HotelName = inflateView.findViewById(R.id.hotel_dialog_hotelName);
            HotelLocation = inflateView.findViewById(R.id.hotel_dialog_hotelLocation);
            ChoseFile = inflateView.findViewById(R.id.hotel_dialog_chose_file);
            Button create = inflateView.findViewById(R.id.hotel_dialog_create);
            Button cancel = inflateView.findViewById(R.id.hotel_dialog_cancel);

            ChoseFile.setOnClickListener(v13 -> loadPhoto());

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
        });

        getHotels();
    } // End onCreate()

    private void getHotels(){

        Call<List<Hotel>> call = RestApiConnection.getInstance().getMyApi().getCreatedHotels(user.getId());

        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Hotel>> call, @NonNull Response<List<Hotel>> response) {

                hoteslList = response.body();

                assert hoteslList != null;
                if(!hoteslList.get(0).getName().equalsIgnoreCase("No Data")) {

                    recyclerView.setVisibility(View.VISIBLE);
                    noHotelYet.setVisibility(View.GONE);

                    HotelAdapterOwner adapter = new HotelAdapterOwner(requireContext(), hoteslList, hotel -> {

                        Intent i = new Intent(requireContext(), HotelRooms.class);
                        i.putExtra("hotelName", hotel.getName());
                        i.putExtra("hotelId", hotel.getId());
                        startActivity(i);
                    });

                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);

                } else {
                    noHotelYet.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Hotel>> call, @NonNull Throwable t) {

                Toast.makeText(requireContext(), "onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CreateHotel(){


        Call<HotelResult> call = RestApiConnection.getInstance().getMyApi()
                .createNewHotel(user.getId(), hSName, "deluxe_delhi.jpeg", hSLocation);

        call.enqueue(new Callback<HotelResult>() {
            @Override
            public void onResponse(@NonNull Call<HotelResult> call, @NonNull Response<HotelResult> response) {

                assert response.body() != null;
                if(!response.body().getError()) {
                    hoteslList.add(response.body().getHotel());
                    try {
                        hoteslList.notify();
                    } catch (IllegalMonitorStateException ex){
                        Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HotelResult> call, @NonNull Throwable t) {

                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPhoto() {

        Intent intent = new Intent();
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 34543);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 34543 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri mImageUri = data.getData();
            Picasso.with(requireContext()).load(mImageUri).into(HotelImage);

        } else {
            Toast.makeText(requireContext(), "Something goes wrong!", Toast.LENGTH_SHORT).show();
        }
    }

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