package com.tara.tara.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tara.tara.FoodCategory;
import com.tara.tara.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelServicesList extends Fragment {

    private TextView viewMenu;

    public HotelServicesList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_services_list, container, false);

        viewMenu = (TextView) view.findViewById(R.id.view_menu);
        viewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FoodCategory.class));
            }
        });
        return view;
    }
}
