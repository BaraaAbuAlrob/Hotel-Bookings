package com.finalandroid.project.hotelbooking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.Hotel;

import java.util.List;

public class HotelOwnerProfile_HotelsAdapter extends ArrayAdapter<Hotel> {

    private final Context context;

    public HotelOwnerProfile_HotelsAdapter(Context context, List<Hotel> hotelList){
        super(context, R.layout.gridview_item_your_hotels, hotelList);

        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // object of model class (Hotel)
        Hotel hotel = getItem(position);

        // if the item is null (has not values)
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gridview_item_your_hotels, parent, false);


        // Link the components with the objects
        ImageView hotelImg = convertView.findViewById(R.id.hotel_grid_item_img);
        TextView hotelName = convertView.findViewById(R.id.hotel_grid_item_name);


        // add the data to the item
        assert hotel != null;
        hotelName.setText(hotel.getName());

        Glide.with(context)
                .load(RestApiConnection.PHOTO_BASE_URL + "hotels/" + hotel.getImg())
                .apply(new RequestOptions().override(100, 100))
                .error(R.drawable.notfound)
                .into(hotelImg);

        return convertView;
    }
}
