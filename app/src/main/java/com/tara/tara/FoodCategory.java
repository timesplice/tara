package com.tara.tara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tara.tara.adapter.CategoriesAdapter;
import com.tara.tara.model.CategoriesModel;
import com.tara.tara.model.FoodMenuModel;

import java.util.HashMap;
import java.util.List;

public class FoodCategory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager stagaggeredGridLayoutManager;

    private CategoriesAdapter categoriesAdapter;
    private List<CategoriesModel> categoriesModels;
    private String hotelId, tableId;
    private HashMap<String, FoodMenuModel> foodMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);
        foodMenu = new HashMap<String, FoodMenuModel>();

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
                    System.out.println("DATASNAPSHOT:" + dataSnapshot.getKey());
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        System.out.println("ARRAY:" + childSnapshot.getKey());
                        foodMenu.put(childSnapshot.getKey(), childSnapshot.getValue(FoodMenuModel.class));
                        categoriesModels.add(new CategoriesModel(childSnapshot.getKey(),
                                foodMenu.get(childSnapshot.getKey()).getName(),
                                foodMenu.get(childSnapshot.getKey()).getImageUrl()));
                    }
                    categoriesAdapter.notifyDataSetChanged();
                } else {
                    System.out.println("DATASNAPSHOT: null");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
