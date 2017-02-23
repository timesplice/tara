package com.tara.tara;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tara.tara.adapter.CartItemsAdapter;
import com.tara.tara.model.FoodMenuModel;
import com.tara.tara.util.CartItems;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    private String hotelId, tableId;
    private RecyclerView recyclerView;
    private CartItemsAdapter cartAdapter;
    private CartItems cartItemsDb;
    List<FoodMenuModel> cartItems = new ArrayList<>();

    public static final String FOOD_ID = "food_id";
    public static final String FOOD_NAME = "food_name";
    public static final String FOOD_DESC = "desc";
    public static final String PRICE = "price";
    public static final String IMAGE_URL = "image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setTitle("Cart");
        Intent data = getIntent();
        if (data != null) {
            hotelId = data.getStringExtra("hotelId");
            tableId = data.getStringExtra("tableId");
        }
        cartItemsDb = new CartItems(this);
        recyclerView = (RecyclerView) findViewById(R.id.cart_item);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        cartAdapter = new CartItemsAdapter(cartItems, this);
        recyclerView.setAdapter(cartAdapter);

        populateCartItems();
    }

    private void populateCartItems() {
        Cursor cursor = cartItemsDb.getAllItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FoodMenuModel foodItem = new FoodMenuModel();
                foodItem.setFoodId(cursor.getString(cursor.getColumnIndex(FOOD_ID)));
                foodItem.setName(cursor.getString(cursor.getColumnIndex(FOOD_NAME)));
                foodItem.setPrice(cursor.getInt(cursor.getColumnIndex(PRICE)));
                foodItem.setImageUrl(cursor.getString(cursor.getColumnIndex(IMAGE_URL)));
                foodItem.setDesc(cursor.getString(cursor.getColumnIndex(FOOD_DESC)));

                cartItems.add(foodItem);
            } while (cursor.moveToNext());
        }
        cartAdapter.notifyDataSetChanged();
    }
}
