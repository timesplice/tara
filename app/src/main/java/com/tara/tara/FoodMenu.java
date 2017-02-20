package com.tara.tara;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tara.tara.adapter.CategoriesAdapter;
import com.tara.tara.adapter.FoodMenuAdapter;
import com.tara.tara.model.FoodMenuModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FoodMenu extends AppCompatActivity {

    private ImageView plus, minus;
    private String hotelId, tableId;
    private ArrayList<String> foodIds;
    private List<FoodMenuModel> foodData = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodMenuAdapter foodMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        setTitle("Food Items");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();
        if (data != null) {
            hotelId = data.getStringExtra("hotelId");
            tableId = data.getStringExtra("tableId");
            foodIds = data.getStringArrayListExtra("foodIdList");
            System.out.println("FOODIDS:"+foodIds.toString());

        }

        recyclerView = (RecyclerView) findViewById(R.id.food_menu);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        foodMenuAdapter = new FoodMenuAdapter(foodData, hotelId, tableId);
        recyclerView.setAdapter(foodMenuAdapter);

        populateData();
    }

    private void populateData() {
        for (final String foodId : foodIds) {
            Login.fDatabase.getReference("menus/" + hotelId + "/" + foodId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    foodData.add(dataSnapshot.getValue(FoodMenuModel.class));
                    System.out.println("foodData:"+foodData.get(foodData.size()-1).getName());
                    if (foodData.size() == foodIds.size()) {
                        foodMenuAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
