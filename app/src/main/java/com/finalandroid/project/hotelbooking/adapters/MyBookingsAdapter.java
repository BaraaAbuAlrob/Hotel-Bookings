package com.finalandroid.project.hotelbooking.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.BookingRooms;

import java.util.List;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder> {

    Context context;
    List<BookingRooms> myBookingRoomsList;
    ItemClickListener itemClickListener;

    public MyBookingsAdapter(Context context, List<BookingRooms> myBookingRoomsList, ItemClickListener itemClickListener) {
        this.context = context;
        this.myBookingRoomsList = myBookingRoomsList;
        this.itemClickListener = itemClickListener;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public MyBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyBookingsViewHolder(LayoutInflater.from(context).inflate(R.layout.mybooking_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingsViewHolder holder, int position) {

        holder.NumDays.setText(myBookingRoomsList.get(holder.getAdapterPosition()).getNumDays());
        holder.PriceNight.setText(myBookingRoomsList.get(holder.getAdapterPosition()).getPriceNight());


        Glide.with(context)
                .load(RestApiConnection.PHOTO_BASE_URL + "rooms/" + myBookingRoomsList.get(holder.getAdapterPosition()).getImg())
                .apply(new RequestOptions().override(1000, 1000))
                .error(R.drawable.notfound)
                .into(holder.RoomImg);

        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(myBookingRoomsList.get(position)));
    }

    @Override
    public int getItemCount() {

        return myBookingRoomsList.size();
    }

    public interface ItemClickListener {

        void onItemClick(BookingRooms bookingRooms);
    }

    public static class MyBookingsViewHolder extends RecyclerView.ViewHolder {

        ImageView RoomImg;
        TextView NumDays, PriceNight;
        public MyBookingsViewHolder(@NonNull View itemView) {
            super(itemView);

            RoomImg = itemView.findViewById(R.id.roomImg);
            NumDays = itemView.findViewById(R.id.roomNumDays);
            PriceNight = itemView.findViewById(R.id.roomPrice);
        }
    }
}