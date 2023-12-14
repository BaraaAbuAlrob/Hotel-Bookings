package com.finalandroid.project.hotelbooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.adapters.RoomAdapter;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.Room;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rooms extends AppCompatActivity {

    TextView hotelName;
    RecyclerView recyclerView;
    List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRooms);
        hotelName = (TextView) findViewById(R.id.heading_hotelName);

        Intent intent = getIntent();
        String HName = intent.getStringExtra("hotelName");
        hotelName.setText(HName);

        getRooms(intent);
    }

    private void getRooms(Intent intent) {

        int hotelId = intent.getIntExtra("hotelID", 0);
        Call<List<Room>> call = RestApiConnection.getInstance().getMyApi().getRooms(hotelId);

        call.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(@NonNull Call<List<Room>> call, @NonNull Response<List<Room>> response) {

                roomList = response.body();
                RoomAdapter adapter = new RoomAdapter(Rooms.this, roomList, room -> {

                    if(room.getHotelId() == 0){
                        Toast.makeText(Rooms.this, R.string.SSTGWPTA, Toast.LENGTH_LONG).show();

                    } else {

                        Intent i = new Intent(Rooms.this, Booking.class);
                        i.putExtra("hotelId", room.getHotelId());
                        i.putExtra("roomId", room.getId());
                        i.putExtra("img", room.getImg());
                        i.putExtra("numDays", room.getNumDays());
                        i.putExtra("price", room.getPriceNight());
                        i.putExtra("description", room.getDescription());
                        startActivity(i);
                    }
                });

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(Rooms.this));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Room>> call, @NonNull Throwable t) {

                Log.d("onFailure:", Objects.requireNonNull(t.getMessage()));
                Toast.makeText(Rooms.this, "onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}