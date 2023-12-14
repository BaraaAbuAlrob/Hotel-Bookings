package com.finalandroid.project.hotelbooking.adapters;

import static java.text.DateFormat.getDateTimeInstance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.activities.CallCustomer;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.BookedRoomResult;
import com.finalandroid.project.hotelbooking.modelClass.User;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookedRoomsAdapter extends RecyclerView.Adapter<BookedRoomsAdapter.BookedRoomsViewHolder> {

    private final Context context;
    private final List<BookedRoomResult> bookedRoomResultList;

    private TextView Name, Email, Phone;

    public BookedRoomsAdapter(Context context, List<BookedRoomResult> bookedRoomResultList) {

        this.context = context;
        this.bookedRoomResultList = bookedRoomResultList;
    }

    @NonNull
    @Override
    public BookedRoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new BookedRoomsViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.booked_rooms_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookedRoomsViewHolder holder, int position) {

        Date date = new Date();
        String DateTime = getDateTimeInstance().format(date);

        holder.UserName.setText(bookedRoomResultList.get(position).getUsername());
        holder.Date.setText(DateTime);
        holder.NumberOfDays.setText(bookedRoomResultList.get(position).getNumDays());

        Glide.with(context)
                .load(RestApiConnection.PHOTO_BASE_URL + "rooms/" + bookedRoomResultList.get(position).getRoomImg())
                .apply(new RequestOptions().override(600, 600))
                .error(R.drawable.notfound)
                .into(holder.RoomImage);

        holder.UserInfo.setOnClickListener(v -> {

            View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog_customer_info, null, false);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(view);
            Dialog dialog = builder.create();
            dialog.show();

            Name = view.findViewById(R.id.CI_name);
            Email = view.findViewById(R.id.CI_email);
            Phone = view.findViewById(R.id.CI_phone);
            Button Close = view.findViewById(R.id.CI_close);
            ImageButton Call = view.findViewById(R.id.CI_call);

            uploadUserInfo(bookedRoomResultList.get(position).getUserId());

            Close.setOnClickListener(v1 -> dialog.cancel());
            Call.setOnClickListener(v2 -> {

                Intent intent = new Intent(context, CallCustomer.class);
                intent.putExtra("phone", Phone.getText().toString());
                context.startActivity(intent);
                dialog.cancel();
            });
        });
    }

    @Override
    public int getItemCount() {

        return bookedRoomResultList.size();
    }

    private void uploadUserInfo(int userId){
        Call<User> call = RestApiConnection.getInstance().getMyApi().getUser(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                if(response.isSuccessful() && response.body() != null) {

                    User user = response.body();
                    Name.setText(user.getName());
                    Email.setText(user.getEmail());
                    Phone.setText(user.getPassword());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class BookedRoomsViewHolder extends RecyclerView.ViewHolder {

        ImageView RoomImage;
        TextView UserName, Date,NumberOfDays;
        Button UserInfo;

        public BookedRoomsViewHolder(@NonNull View itemView) {
            super(itemView);

            RoomImage = itemView.findViewById(R.id.BR_RVI_RoomImage);
            UserName = itemView.findViewById(R.id.booked_rooms_username);
            Date = itemView.findViewById(R.id.booked_rooms_bookingDate);
            NumberOfDays = itemView.findViewById(R.id.booked_rooms_numDays);
            UserInfo = itemView.findViewById(R.id.booked_rooms_userInfo);
        }
    }
}