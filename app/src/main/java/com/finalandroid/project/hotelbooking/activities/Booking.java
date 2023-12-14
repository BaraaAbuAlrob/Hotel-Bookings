package com.finalandroid.project.hotelbooking.activities;

import static java.text.DateFormat.getDateTimeInstance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.Result;
import com.finalandroid.project.hotelbooking.sharedPref.SharedPrefManager;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Booking extends AppCompatActivity {

    private final String CHANEL_ID = "hotelBookings_01";
    private static int notification_ID = 95733;

    private int hotelId, roomId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);

        ImageView roomImg1 = findViewById(R.id.booking_roomImg);
        TextView numDays = findViewById(R.id.booking_NumOfDays);
        TextView priceNight = findViewById(R.id.booking_roomPrice);
        TextView description = findViewById(R.id.booking_description);
        Button booking = findViewById(R.id.bookTheRoom);

        Intent getData = getIntent();

        hotelId = getData.getIntExtra("hotelId", 0);
        roomId = getData.getIntExtra("roomId", 0);
        String roomImg = getData.getStringExtra("img");
        String roomNumDay = getData.getStringExtra("numDays");
        String roomPrice = getData.getStringExtra("price");
        String roomDescription = getData.getStringExtra("description");

        Glide.with(Booking.this)
                .load(RestApiConnection.PHOTO_BASE_URL + "rooms/" + roomImg)
                .apply(new RequestOptions().override(600, 600))
                .error(R.drawable.notfound)
                .into(roomImg1);

        numDays.setText(roomNumDay);
        priceNight.setText(roomPrice);
        description.setText(roomDescription);

        booking.setOnClickListener(v -> {

            userId = SharedPrefManager.getInstance(Booking.this).getUser().getId();
            Call<Result> callRoom = RestApiConnection.getInstance().getMyApi().booking(hotelId, roomId, userId);

            callRoom.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                    assert response.body() != null;
                    if (!response.body().getError())
                        addNotification(response.body().getMessage());
                }

                @Override
                public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                    addNotification(t.getMessage());
                }
            });
        });
    }

    private void addNotification(String Text){

//                     Good
//                    Date date = new Date();
//                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
//                    SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
//                    String theDate = sdfDate.format(date);
//                    String theTime = sdfTime.format(date);

        Date date = new Date();
        String title = "Booking room";
        String DateTime = getDateTimeInstance().format(date);

        Call<Result> callNotification = RestApiConnection.getInstance().getMyApi().addNotification(userId, title, Text, DateTime);
        callNotification.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                assert response.body() != null;
                if(!response.body().getError()){

                    NotificationChannel mChanel = new NotificationChannel(CHANEL_ID, "hotelBookings chanel", NotificationManager.IMPORTANCE_HIGH);
                    Notification notification = new NotificationCompat.Builder(Booking.this, CHANEL_ID)
                            .setSmallIcon(R.drawable.hotel)
                            .setContentTitle(title)
                            .setContentText(Text)
                            .build();

                    Intent intent = new Intent(Booking.this, MainActivity.class);
                    intent.putExtra("noti", 777);

                    PendingIntent pendingIntent = PendingIntent.getActivity(Booking.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                    notification.contentIntent = pendingIntent;

                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.createNotificationChannel(mChanel);

                    mNotificationManager.notify(notification_ID, notification);
                    notification_ID++;
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                Toast.makeText(Booking.this, "onFailure: "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
