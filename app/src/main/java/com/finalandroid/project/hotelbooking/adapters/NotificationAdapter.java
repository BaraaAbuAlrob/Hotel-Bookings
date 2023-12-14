package com.finalandroid.project.hotelbooking.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.activities.MyBookings;
import com.finalandroid.project.hotelbooking.modelClass.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final Context context;
    private final List<NotificationModel> notificationsList;
    private final ItemClickListener itemClickListener;

    public NotificationAdapter(Context context, List<NotificationModel> notificationsList, ItemClickListener itemClickListener) {

        this.context = context;
        this.notificationsList = notificationsList;
        this.itemClickListener = itemClickListener;
    }

    public void addItem(NotificationModel notification){

        notificationsList.add(notification);
    }
    public void deleteItem(NotificationModel notification){

        notificationsList.remove(notification);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new NotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        holder.Title.setText(notificationsList.get(position).getTitle());
        holder.Text.setText(notificationsList.get(position).getText());
        holder.DateTime.setText(notificationsList.get(position).getDate_time());

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(notificationsList.get(position)));

        holder.myBookings.setOnClickListener(v -> context.startActivity(new Intent(context, MyBookings.class)));
    }

    @Override
    public int getItemCount() {

        return notificationsList.size();
    }

    public interface ItemClickListener{

        void onItemClick(NotificationModel notification);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView Title, Text, DateTime;
        Button myBookings;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.notRec_item_title);
            Text = itemView.findViewById(R.id.notRec_item_text);
            DateTime = itemView.findViewById(R.id.notRec_item_dateTime);
            myBookings = itemView.findViewById(R.id.notRec_item_myBookings);
        }
    }
}
