package com.finalandroid.project.hotelbooking.adapters;

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
import com.finalandroid.project.hotelbooking.modelClass.Hotel;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private final Context context;
    private final List<Hotel> hotelList;
    private final ItemClickListener itemClickListener;

    public HotelAdapter(Context context, List<Hotel> hotelList, ItemClickListener itemClickListener) {
        this.context = context;
        this.hotelList = hotelList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        الربط بين ال hotel_recyclerview_item وال adapter
        return new HotelViewHolder(LayoutInflater.from(context).inflate(R.layout.hotel_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {

        holder.HotelName.setText(hotelList.get(position).getName());
        holder.HotelLocation.setText(hotelList.get(position).getLocation());
//        holder.HotelImg.setImageResource(hotelList.get(position).getImg());

        Glide.with(context)
                .load(RestApiConnection.PHOTO_BASE_URL + "hotels/" + hotelList.get(position).getImg())
                .apply(new RequestOptions().override(600, 600))
                .error(R.drawable.notfound)
                .into(holder.HotelImg);

        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(hotelList.get(position)));
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
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            HotelImg = (ImageView) itemView.findViewById(R.id.hotel_item_img);
            HotelName = (TextView) itemView.findViewById(R.id.hotel_item_name);
            HotelLocation = (TextView) itemView.findViewById(R.id.hotel_item_location);
        }
    }
}
