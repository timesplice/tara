package com.tara.tara.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tara.tara.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelServicesList extends Fragment {


    public HotelServicesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_services_list, container, false);
    }

}
