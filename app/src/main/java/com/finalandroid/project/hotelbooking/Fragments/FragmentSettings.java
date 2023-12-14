package com.finalandroid.project.hotelbooking.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.finalandroid.project.hotelbooking.R;
import com.finalandroid.project.hotelbooking.activities.MyBookings;
import com.finalandroid.project.hotelbooking.activities.Profile;
import com.finalandroid.project.hotelbooking.sharedPref.SharedPrefManager;

public class FragmentSettings extends Fragment implements View.OnClickListener {

    ImageView UserPhoto;
    Button myBookings, aboutUs, logout;

    TextView Name, Email;

//    DefaultDialogFragment dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false); // True
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserPhoto = view.findViewById(R.id.fragment_userPhoto);
        myBookings = view.findViewById(R.id.fragment_myBookings);
        aboutUs = view.findViewById(R.id.sett_about_us);
        logout = view.findViewById(R.id.sett_logout);
        Name = view.findViewById(R.id.sett_username);
        Email = view.findViewById(R.id.sett_email);

        /*
        String userImg = SharedPrefManager.getInstance(requireActivity().getApplicationContext()).getUser().getImg();
        Uri userUriImg = Uri.parse(userImg);

        Picasso.with(requireContext()).load(userUriImg).into(UserPhoto);

        Glide.with(requireActivity().getApplicationContext())
                .load(RestApiConnection.PHOTO_BASE_URL + "users/" + userImg)
                .apply(new RequestOptions().override(90, 90))
                .error(R.drawable.notfound)
                .into(UserPhoto);
        */


        Name.setText(SharedPrefManager.getInstance(getContext()).getUser().getName());
        Email.setText(SharedPrefManager.getInstance(getContext()).getUser().getEmail());

//        Name.setHighlightColor(R.color.mainColorOrange);

        myBookings.setOnClickListener(this);
        UserPhoto.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == UserPhoto){
            startActivity(new Intent(getContext(), Profile.class));

        } else if (v == myBookings) {
            startActivity(new Intent(getContext(), MyBookings.class));

        } else if (v == aboutUs) {
            new AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.ic_info)
                    .setTitle(R.string.about_Us)
                    .setMessage(R.string.about_Us_message)
                    .setNegativeButton(R.string.back, (dialog, which) -> dialog.cancel())
                    .create()
                    .show();

        } else if (v == logout) {

            new AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.ic_warning)
                    .setTitle(R.string.note)
                    .setMessage(R.string.areYouSure)
                    .setPositiveButton(R.string.logout, (dialog, which) -> {

                        requireActivity().finish();
                        SharedPrefManager.getInstance(getContext()).logout();

                        /** Can return null */
//                            getActivity();
//                            getContext();
//                            getArguments();

                        /** Can't return null */
//                            requireActivity();
//                            requireContext();
//                            requireArguments();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                    .create()
                    .show();
        }
    }
}