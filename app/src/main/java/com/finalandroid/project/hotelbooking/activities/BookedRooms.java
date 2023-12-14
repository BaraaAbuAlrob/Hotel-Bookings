package com.finalandroid.project.hotelbooking.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.adapters.BookedRoomsAdapter;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.BookedRoomResult;
import com.finalandroid.project.hotelbooking.modelClass.Room;
import com.finalandroid.project.hotelbooking.modelClass.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookedRooms extends AppCompatActivity {

    private int hotelId;
    private TextView HotelName, NoDataYet;
    private RecyclerView RecyclerView;
    private List<BookedRoomResult> bookedRoomsList = new ArrayList<>();

    private final List<User> usersList = new ArrayList<>();
    private final List<Room> roomsList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booked_rooms);

        HotelName = findViewById(R.id.booked_rooms_hotelName);
        NoDataYet = findViewById(R.id.BRs_text_noRoomsYet);
        RecyclerView = findViewById(R.id.recyclerViewBookedRooms);

        String hotelName;

        Intent intent = getIntent();
        hotelId = intent.getIntExtra("hotelId", 0);
        HotelName.setText(intent.getStringExtra("hotelName"));

        loadBookedRooms();
    }

    private void loadBookedRooms() {

        Call<List<BookedRoomResult>> call = RestApiConnection.getInstance().getMyApi().getBookedRooms(hotelId);
        call.enqueue(new Callback<List<BookedRoomResult>>() {
            @Override
            public void onResponse(@NonNull Call<List<BookedRoomResult>> call, @NonNull Response<List<BookedRoomResult>> response) {

                bookedRoomsList = response.body();
                if (bookedRoomsList != null && !bookedRoomsList.isEmpty() && bookedRoomsList.get(0).getUserId() != 0) {

                    NoDataYet.setVisibility(View.GONE);
                    RecyclerView.setVisibility(View.VISIBLE);
                    HotelName.setVisibility(View.VISIBLE);

                    BookedRoomsAdapter adapter = new BookedRoomsAdapter(BookedRooms.this, bookedRoomsList);
                    RecyclerView.setHasFixedSize(true);
                    RecyclerView.setLayoutManager(new LinearLayoutManager(BookedRooms.this));
                    RecyclerView.setAdapter(adapter);

                } else {

                    NoDataYet.setVisibility(View.VISIBLE);
                    RecyclerView.setVisibility(View.GONE);
                    HotelName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BookedRoomResult>> call, @NonNull Throwable t) {

                Toast.makeText(BookedRooms.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}