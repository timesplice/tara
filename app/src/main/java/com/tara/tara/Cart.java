package com.tara.tara;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tara.tara.adapter.CartItemsAdapter;
import com.tara.tara.model.FoodMenuModel;
import com.tara.tara.util.CartItems;
import com.tara.tara.util.ScanPreference;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    private String hotelId, tableId;
    private RecyclerView recyclerView;
    private CartItemsAdapter cartAdapter;
    private CartItems cartItemsDb;
    List<FoodMenuModel> cartItems = new ArrayList<>();
    private TextView total;
    private LinearLayout payment;
    private int grandTotal = 0;
    private ScanPreference scanPreference;

    public static final String FOOD_ID = "food_id";
    public static final String FOOD_NAME = "food_name";
    public static final String FOOD_DESC = "description";
    public static final String PRICE = "price";
    public static final String IMAGE_URL = "image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setTitle("Cart");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        scanPreference = new ScanPreference(this);
        hotelId = scanPreference.getScanDetails().getHotelId();
        tableId = scanPreference.getScanDetails().getTableId();

        cartItemsDb = new CartItems(this);
        recyclerView = (RecyclerView) findViewById(R.id.cart_item);
        total = (TextView) findViewById(R.id.total);
        payment = (LinearLayout) findViewById(R.id.footer_cart);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        cartAdapter = new CartItemsAdapter(cartItems, this);
        recyclerView.setAdapter(cartAdapter);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cart.this, ChoosePayment.class);
                intent.putExtra("hotelId", hotelId);
                intent.putExtra("tableId", tableId);
                intent.putExtra("total", grandTotal);
                startActivity(intent);
            }
        });

        populateCartItems();
    }

    private void populateCartItems() {
        Cursor cursor = cartItemsDb.getAllItems();
        System.out.println("POPULATE CAT::");
        double totalCartPrice = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FoodMenuModel foodItem = new FoodMenuModel();
                foodItem.setFoodId(cursor.getString(cursor.getColumnIndex(FOOD_ID)));
                foodItem.setName(cursor.getString(cursor.getColumnIndex(FOOD_NAME)));
                foodItem.setPrice(cursor.getInt(cursor.getColumnIndex(PRICE)));
                foodItem.setImageUrl(cursor.getString(cursor.getColumnIndex(IMAGE_URL)));
                foodItem.setDesc(cursor.getString(cursor.getColumnIndex(FOOD_DESC)));
                System.out.println("FOOD IN CART:" + foodItem.getFoodId());
                cartItems.add(foodItem);
                totalCartPrice += foodItem.getPrice();
            } while (cursor.moveToNext());
        }
        total.setText("Your grand total is : " + (int) totalCartPrice + " \u20B9");
        grandTotal = (int) totalCartPrice;
        cartAdapter.notifyDataSetChanged();
    }


    public void removeItem(FoodMenuModel cartItem) {
        cartItemsDb.deleteByProductId(cartItem.getFoodId());
        cartItems.clear();
        populateCartItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
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
