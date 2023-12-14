package com.finalandroid.project.hotelbooking.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.adapters.NotificationAdapter;
import com.finalandroid.project.hotelbooking.apis.RestApiConnection;
import com.finalandroid.project.hotelbooking.modelClass.NotificationModel;
import com.finalandroid.project.hotelbooking.modelClass.Result;
import com.finalandroid.project.hotelbooking.sharedPref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNotificationsOwner extends Fragment {

    RecyclerView recyclerView;
    TextView noDataYet;
    List<NotificationModel> notificationsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false); // True
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fra_not_recyclerView);
        noDataYet = view.findViewById(R.id.text_noData_yet);

        getNotifications();
    }

    private void getNotifications() {

        int userId = SharedPrefManager.getInstance(requireContext()).getUser().getId();
        Call<List<NotificationModel>> call = RestApiConnection.getInstance().getMyApi().getNotifications(userId);

        call.enqueue(new Callback<List<NotificationModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<NotificationModel>> call, @NonNull Response<List<NotificationModel>> response) {

                notificationsList = response.body();

                assert notificationsList != null;
                if (notificationsList.size() > 0 && !notificationsList.get(0).getText().equalsIgnoreCase("No data")){
                    noDataYet.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                } else {
                    recyclerView.setVisibility(View.GONE);
                    noDataYet.setVisibility(View.VISIBLE);
                }

                NotificationAdapter adapter = new NotificationAdapter(getContext(),notificationsList, notification ->

                        new AlertDialog.Builder(requireContext())
                                .setIcon(R.drawable.ic_warning)
                                .setTitle(R.string.note)
                                .setMessage(R.string.deleteTheNotification)
                                .setPositiveButton(R.string.delete, (dialog, which) -> deleteNotification(notification.getId()))
                                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                                .show() );

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<NotificationModel>> call, @NonNull Throwable t) {

                Toast.makeText(requireContext(), "لا توجد بيانات حتى الآن", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteNotification(int id){

        Call<Result> call = RestApiConnection.getInstance().getMyApi().deleteNotification(id);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                assert response.body() != null;
                if(!response.body().getError())
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
