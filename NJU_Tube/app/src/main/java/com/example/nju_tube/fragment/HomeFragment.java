package com.example.nju_tube.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nju_tube.NJUTube;
import com.example.nju_tube.R;

import java.util.Objects;


public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        TextView userName = view.findViewById(R.id.home_user_name);
        userName.setText(((NJUTube) Objects.requireNonNull(getActivity()).getApplication()).getUserName());
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.home_video_view_frame, new UploadedByMeFragment());
        transaction.commit();
        return view;
    }
}