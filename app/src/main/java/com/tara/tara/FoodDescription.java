package com.tara.tara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tara.tara.model.FoodMenuModel;

public class FoodDescription extends AppCompatActivity {
    private String hotelId,tableId;
    FoodMenuModel foodItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_description);

        setTitle("Food Description");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();
        if (data != null) {
            hotelId = data.getStringExtra("hotelId");
            tableId = data.getStringExtra("tableId");
            foodItem = (FoodMenuModel) data.getSerializableExtra("foodItemModel");
            System.out.println("FOOD ITEM::::::::::::::"+foodItem.getName());

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

