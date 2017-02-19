package com.tara.tara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tara.tara.adapter.CategoriesAdapter;
import com.tara.tara.model.CategoriesModel;
import com.tara.tara.model.HotelModel;

import java.util.List;

public class FoodCategory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager stagaggeredGridLayoutManager;
    private List<CategoriesModel> categories;
    private CategoriesAdapter categoriesAdapter;
    private List<CategoriesModel> categoriesModels;
    private String hotelId, tableId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);

        Intent hotelDetails = getIntent();
        if (hotelDetails != null) {
            hotelId = hotelDetails.getStringExtra("hotelId");
            tableId = hotelDetails.getStringExtra("tableId");
        }
        getMenuFromFirebase();
        recyclerView = (RecyclerView) findViewById(R.id.categories);
        recyclerView.setHasFixedSize(true);

        stagaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(stagaggeredGridLayoutManager);

        categoriesAdapter = new CategoriesAdapter(categoriesModels);
        recyclerView.setAdapter(categoriesAdapter);
    }

    private void getMenuFromFirebase() {
        Login.fDatabase.getReference("menus/" + hotelId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getKey() != null) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        System.out.println("ARRAY:"+postSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
