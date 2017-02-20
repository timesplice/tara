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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodCategory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager stagaggeredGridLayoutManager;

    private CategoriesAdapter categoriesAdapter;
    private List<CategoriesModel> categoriesModels=new ArrayList<>();
    private String hotelId, tableId;
    private HashMap<String, ArrayList<FoodMenuModel>> foodMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);
        foodMenu = new HashMap<String, ArrayList<FoodMenuModel>>();

        setTitle("Categories");
        Intent hotelDetails = getIntent();
        if (hotelDetails != null) {
            hotelId = hotelDetails.getStringExtra("hotelId");
            tableId = hotelDetails.getStringExtra("tableId");
        }
        getMenuFromFirebase();
        recyclerView = (RecyclerView) findViewById(R.id.categories);
        recyclerView.setHasFixedSize(true);

        stagaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(stagaggeredGridLayoutManager);

        categoriesAdapter = new CategoriesAdapter(categoriesModels, foodMenu, hotelId, tableId);
        recyclerView.setAdapter(categoriesAdapter);
    }

    private void getMenuFromFirebase() {
        Login.fDatabase.getReference("menus/" + hotelId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getKey() != null) {
                    System.out.println("DATASNAPSHOT:" + dataSnapshot.getKey());
                    Set<String> categories = new HashSet<>();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        FoodMenuModel food = childSnapshot.getValue(FoodMenuModel.class);

                        System.out.println("ARRAY:" + childSnapshot.getKey());
                        String category = food.getCategory().toLowerCase().replace(" ", "") + ".jpg";
                        if (!categories.contains(category)) {
                            ArrayList<FoodMenuModel> newCategoryFoods = new ArrayList<>();
                            newCategoryFoods.add(food);
                            foodMenu.put(food.getCategory(), newCategoryFoods);

                            categoriesModels.add(new CategoriesModel(childSnapshot.getKey(),
                                    food.getCategory(),
                                    hotelId + "/category/" + category));
                            categories.add(category);
                        } else {
                            foodMenu.get(food.getCategory()).add(food);
                        }
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
