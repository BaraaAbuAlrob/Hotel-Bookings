package com.finalandroid.project.hotelbooking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.activities.BookedRooms;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.Hotel;
import com.finalandroid.project.hotelbooking.modelClass.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelAdapterOwner extends RecyclerView.Adapter<HotelAdapterOwner.HotelViewHolder> {

    private final Context context;
    private final List<Hotel> hotelList;
    private final ItemClickListener itemClickListener;

    public HotelAdapterOwner(Context context, List<Hotel> hotelList, ItemClickListener itemClickListener) {
        this.context = context;
        this.hotelList = hotelList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        الربط بين ال hotel_recyclerview_item_owner وال adapter
        return new HotelViewHolder(LayoutInflater.from(context).inflate(R.layout.hotel_recyclerview_item_owner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.HotelName.setText(hotelList.get(position).getName());
        holder.HotelLocation.setText(hotelList.get(position).getLocation());

        Glide.with(context)
                .load(RestApiConnection.PHOTO_BASE_URL + "hotels/" + hotelList.get(position).getImg())
                .apply(new RequestOptions().override(600, 600))
                .error(R.drawable.notfound)
                .into(holder.HotelImg);

        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(hotelList.get(position)));

        int hotelId = hotelList.get(position).getId();
        holder.DeleteHotel.setOnClickListener(v ->

                new AlertDialog.Builder(context)
                .setMessage(R.string.areYouSure)
                .setPositiveButton(R.string.yes, (dialog, which) -> {

                    Call<Result> call = RestApiConnection.getInstance().getMyApi().deleteHotel(hotelId);

                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                            assert response.body() != null;
                            if(!response.body().getError()) {
                                hotelList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, getItemCount());
                            }

                            dialog.cancel();
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                })
                .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel())
                .create()
                .show());


        holder.TheBookedRooms.setOnClickListener(v -> {

            Intent intent = new Intent(context, BookedRooms.class);
            intent.putExtra("hotelId", hotelId);
            intent.putExtra("hotelName", hotelList.get(position).getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return hotelList.size();
    }

    public interface ItemClickListener{

        void onItemClick(Hotel hotel);
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {

        ImageView HotelImg;
        TextView HotelName, HotelLocation;
        Button DeleteHotel, TheBookedRooms;
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            HotelImg = itemView.findViewById(R.id.hotel_item_img);
            HotelName = itemView.findViewById(R.id.hotel_item_name);
            HotelLocation = itemView.findViewById(R.id.hotel_item_location);
            DeleteHotel = itemView.findViewById(R.id.hotel_item_deleteHotel);
            TheBookedRooms = itemView.findViewById(R.id.hotel_item_roomsBooked);
        }
    }
}