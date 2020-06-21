package com.example.coupleapp.Activity.DateCourse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coupleapp.R;

import androidx.fragment.app.Fragment;


public class DatePlaceFragment extends Fragment {

    public DatePlaceFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_place, container, false);
    }


}
