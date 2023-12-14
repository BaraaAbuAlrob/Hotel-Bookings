package com.finalandroid.project.hotelbooking.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.activities.Rooms;
import com.finalandroid.project.hotelbooking.adapters.HotelAdapter;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.Hotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHotels extends Fragment {

    private RecyclerView recyclerView;
    TextView noDataYet;
    private List<Hotel> hotelList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hotels, container, false); // True
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewHotels);
        noDataYet = view.findViewById(R.id.text_noData_yet);

        getHotels();
    }

    private void getHotels(){

        Call<List<Hotel>> call = RestApiConnection.getInstance().getMyApi().getHotels();

        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(@NonNull Call<List<Hotel>> call, @NonNull Response<List<Hotel>> response) {

                hotelList = response.body();
                assert hotelList != null;
                if (hotelList.size() > 0 && !hotelList.get(0).getImg().equalsIgnoreCase("No data")){
                    noDataYet.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    noDataYet.setVisibility(View.VISIBLE);
                }

                HotelAdapter adapter = new HotelAdapter(getContext(), hotelList, hotel -> {

                    Intent i = new Intent(getContext(), Rooms.class);
                    i.putExtra("hotelName", hotel.getName());
                    i.putExtra("hotelID", hotel.getId());
                    startActivity(i);
                });

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Hotel>> call, @NonNull Throwable t) {

                Toast.makeText(getContext(), "onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}