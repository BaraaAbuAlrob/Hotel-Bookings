package com.finalandroid.project.hotelbooking.activities;

import static java.text.DateFormat.getDateTimeInstance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.adapters.MyBookingsAdapter;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.BookingRooms;
import com.finalandroid.project.hotelbooking.modelClass.Result;
import com.finalandroid.project.hotelbooking.sharedPref.SharedPrefManager;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookings extends AppCompatActivity {

    private final String CHANEL_ID = "hotelBookings_01";
    private static int notification_ID = 95733;

    private RecyclerView recyclerView;
    TextView noDataYet;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bookings);

        recyclerView = findViewById(R.id.myBookings_recyclerView);
        noDataYet = findViewById(R.id.text_noData_yet);

        getMyBookings();
    }


    public void getMyBookings() {

        int userId = SharedPrefManager.getInstance(MyBookings.this).getUser().getId();
        Call<List<BookingRooms>> call = RestApiConnection.getInstance().getMyApi().getMyBookings(userId);

        call.enqueue(new Callback<List<BookingRooms>>() {
            @Override
            public void onResponse(@NonNull Call<List<BookingRooms>> call, @NonNull Response<List<BookingRooms>> response) {

                List<BookingRooms> myBookingsList = response.body();
                assert myBookingsList != null;
                if (myBookingsList.size() > 0 && !myBookingsList.get(0).getImg().equalsIgnoreCase("No data")){
                    noDataYet.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    noDataYet.setVisibility(View.VISIBLE);
                }

                MyBookingsAdapter adapter = new MyBookingsAdapter(MyBookings.this, myBookingsList, bookingRooms ->

                    new AlertDialog.Builder(MyBookings.this)
                            .setIcon(R.drawable.ic_warning)
                            .setTitle(R.string.note)
                            .setMessage(R.string.cancelBookingRoom)
                            .setPositiveButton(R.string.yes, (dialog, which) -> cancelBookingRoom(bookingRooms.getRoomId()))
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel())
                            .show() );

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MyBookings.this));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<BookingRooms>> call, @NonNull Throwable t) {

                Toast.makeText(MyBookings.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelBookingRoom(int roomId){


        userId = SharedPrefManager.getInstance(this).getUser().getId();
        Call<Result> call = RestApiConnection.getInstance().getMyApi().cancelBookingRoom(roomId, userId);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                assert response.body() != null;
                addNotification(response.body().getMessage());
                recreate();
//                Toast.makeText(MyBookings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                Toast.makeText(MyBookings.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addNotification(String Text){

        Date date = new Date();
        String title = "Cancellation of room booking";
        String DateTime = getDateTimeInstance().format(date);


        Call<Result> callNotification = RestApiConnection.getInstance().getMyApi().addNotification(userId, title, Text, DateTime);
        callNotification.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                assert response.body() != null;
                if(!response.body().getError()){

                    NotificationChannel mChanel = new NotificationChannel(CHANEL_ID, "hotelBookings chanel", NotificationManager.IMPORTANCE_HIGH);
                    Notification notification = new NotificationCompat.Builder(MyBookings.this, CHANEL_ID)
                            .setSmallIcon(R.drawable.hotel)
                            .setContentTitle(title)
                            .setContentText(Text)
                            .build();

                    Intent intent = new Intent(MyBookings.this, MainActivity.class);
                    intent.putExtra("noti", 777);

                    PendingIntent pendingIntent = PendingIntent.getActivity(MyBookings.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                    notification.contentIntent = pendingIntent;

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.createNotificationChannel(mChanel);

                    mNotificationManager.notify(notification_ID, notification);
                    notification_ID++;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                Toast.makeText(MyBookings.this, "onFailure: "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}