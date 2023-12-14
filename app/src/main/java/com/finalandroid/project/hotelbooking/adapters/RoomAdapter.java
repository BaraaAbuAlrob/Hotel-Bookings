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
import com.finalandroid.project.hotelbooking.modelClass.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private final Context context;
    private final List<Room> roomsList;
    private final RoomAdapter.ItemClickListener itemClickListener;

    public RoomAdapter(Context context, List<Room> roomsList, ItemClickListener itemClickListener) {
        this.context = context;
        this.roomsList = roomsList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new RoomViewHolder(LayoutInflater.from(context).inflate(R.layout.room_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {


        holder.NumDays.setText(roomsList.get(position).getNumDays());
        holder.PriceNight.setText(roomsList.get(position).getPriceNight());

        Glide.with(context)
                .load(RestApiConnection.PHOTO_BASE_URL + "rooms/" + roomsList.get(position).getImg())
                .apply(new RequestOptions().override(600, 600))
                .error(R.drawable.notfound)
                .into(holder.RoomImg);

        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClick(roomsList.get(position)));
    }

    @Override
    public int getItemCount() {

        return roomsList.size();
    }

    public interface ItemClickListener {

        void onItemClick(Room room);
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {

        ImageView RoomImg;
        TextView NumDays, PriceNight;
        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);

            RoomImg = (ImageView) itemView.findViewById(R.id.room_item_img);
            NumDays = (TextView) itemView.findViewById(R.id.room_numDays);
            PriceNight = (TextView) itemView.findViewById(R.id.room_thePrice);
        }
    }
}