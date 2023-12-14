package com.finalandroid.project.hotelbooking.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.adapters.RoomAdapter;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.Result;
import com.finalandroid.project.hotelbooking.modelClass.Room;
import com.finalandroid.project.hotelbooking.modelClass.RoomResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelRooms extends AppCompatActivity {

    private final int REQUEST_CODE_PICK = 2135;
    private int hotelId;
    private List<Room> roomsList;
    private RecyclerView RecyclerView;
    TextView noRoomsYet;
    private ImageView RoomImg;
    private Button ChoseFile;

    private TextInputLayout NumDays,PriceFANight,Description;
    private String SNumDays,SPriceFANight,SDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_rooms);

        TextView headingText_HotelName = findViewById(R.id.heading_hotelNameOwner);
        FloatingActionButton createNewRoom = findViewById(R.id.createRoomOwner);
        RecyclerView = findViewById(R.id.recyclerViewRoomsOwner);
        noRoomsYet = findViewById(R.id.text_noRoomsYetOwner);

        Intent intent = getIntent();
        hotelId = intent.getIntExtra("hotelId", 0);
        headingText_HotelName.setText(intent.getStringExtra("hotelName"));

        loadRooms();

        createNewRoom.setOnClickListener(v1 -> {

            View view = LayoutInflater.from(HotelRooms.this).inflate(R.layout.custom_dialog_create_new_room, null, false);

            RoomImg = view.findViewById(R.id.CNR_RoomImg);
            ChoseFile = view.findViewById(R.id.CNR_Chose_file);

            NumDays = view.findViewById(R.id.CNR_NumDays);
            PriceFANight = view.findViewById(R.id.CNR_Price);
            Description = view.findViewById(R.id.CNR_Description);

            Button Create = view.findViewById(R.id.CNR_create);
            Button Cancel = view.findViewById(R.id.CNR_cancel);

            AlertDialog.Builder builder = new AlertDialog.Builder(HotelRooms.this);
            builder.setView(view);
            Dialog dialog = builder.create();
            dialog.show();

            ChoseFile.setOnClickListener(v2 -> openFileChooser());

            Create.setOnClickListener(v3 -> {

                if(!NumDaysValidate() || !PriceFANightValidate() || !descriptionValidate());
                else {
                    createRoom();
                    dialog.cancel();
                }
            });

            Cancel.setOnClickListener(v4 -> dialog.cancel());
        });
    }

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

            Uri mImageUri = data.getData();

//            String image = "";
//            if(mImageUri != null)
//                image = mImageUri.toString();

            Picasso.with(HotelRooms.this).load(mImageUri).into(RoomImg); ///////    Picasso   \\\\\\\\
        }
    }

    private void createRoom() {
        String defaultImg = "d1.jpeg";
        Call<RoomResult> call = RestApiConnection.getInstance().getMyApi()
                .createNewRoom(hotelId, defaultImg, SNumDays, SPriceFANight+"$", SDescription);

        call.enqueue(new Callback<RoomResult>() {
            @Override
            public void onResponse(@NonNull Call<RoomResult> call, @NonNull Response<RoomResult> response) {

                assert response.body() != null;
                if (!response.body().getError())
                    recreate();

                Toast.makeText(HotelRooms.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<RoomResult> call, @NonNull Throwable t) {

                Toast.makeText(HotelRooms.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRooms() {

        Call<List<Room>> call = RestApiConnection.getInstance().getMyApi().getRooms(hotelId);
        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(@NonNull Call<List<Room>> call, @NonNull Response<List<Room>> response) {

                roomsList = response.body();

                assert roomsList != null;
                if(roomsList.get(0).getId() == 0){
                    noRoomsYet.setVisibility(View.VISIBLE);
                    RecyclerView.setVisibility(View.GONE);

                } else {

                    RoomAdapter adapter = new RoomAdapter(HotelRooms.this, roomsList, room -> {

                        View view = LayoutInflater.from(HotelRooms.this).inflate(R.layout.custom_dialog_delete_update_room, null, false);
                        Button update = view.findViewById(R.id.dialog_UD_room_update);
                        Button delete = view.findViewById(R.id.dialog_UD_room_delete);

                        AlertDialog.Builder builder = new AlertDialog.Builder(HotelRooms.this);
                        builder.setView(view);
                        Dialog dialog = builder.create();
                        dialog.show();

                        update.setOnClickListener(v -> {

                            Intent intent = new Intent(HotelRooms.this, UpdateRoomInfo.class);
                            intent.putExtra("roomId", room.getId());
                            intent.putExtra("hotelId", room.getHotelId());

                            intent.putExtra("numDays", room.getNumDays());
                            intent.putExtra("priceNight", room.getPriceNight());
                            intent.putExtra("description", room.getDescription());
                            startActivity(intent);
                            dialog.cancel();
                        });

                        delete.setOnClickListener(v -> {

                            dialog.cancel();
                            new AlertDialog.Builder(HotelRooms.this)
//                            .setIcon(R.drawable.ic_info)
//                            .setTitle(R.string.about_Us)
                                    .setMessage(R.string.areYouSure)
                                    .setPositiveButton(R.string.yes, (dial1, which) -> {

                                        deleteRoom(room.getId());
                                        dial1.cancel();
                                        recreate();
                                    })
                                    .setNegativeButton(R.string.back, (dial2, which) -> dial2.cancel())
                                    .show();
                        });
                    });

                    RecyclerView.setHasFixedSize(true);
                    RecyclerView.setLayoutManager(new LinearLayoutManager(HotelRooms.this));
                    RecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Room>> call, @NonNull Throwable t) {

                Toast.makeText(HotelRooms.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteRoom(int roomId){

        Call<Result> call = RestApiConnection.getInstance().getMyApi().deleteRoom(roomId);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                assert response.body() != null;
                if(!response.body().getError())
                    recreate();

                Toast.makeText(HotelRooms.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                Toast.makeText(HotelRooms.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        recreate();
    }

    private boolean NumDaysValidate() {
        SNumDays = Objects.requireNonNull(NumDays.getEditText()).getText().toString().trim();

        if (SNumDays.isEmpty()) {
            NumDays.setError("Required field!. This field should not be empty!");
            return false;

        } else if (SNumDays.length() > 2) {
            NumDays.setError("Number of days field should be less than 3 characters");
            return false;

        } else {
            NumDays.setErrorEnabled(false);
            return true;
        }
    }

    private boolean PriceFANightValidate(){
        SPriceFANight = Objects.requireNonNull(PriceFANight.getEditText()).getText().toString().trim();

        if(SPriceFANight.isEmpty()){
            PriceFANight.setError("Required field!. This field should not be empty!");
            return false;

        } else if(SPriceFANight.length() > 3){
            PriceFANight.setError("The price fora a night field should be less than 4 characters");
            return false;

        } else {
            PriceFANight.setErrorEnabled(false);
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
    }
}