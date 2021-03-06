package com.tara.tara;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.tara.tara.model.FoodMenuModel;
import com.tara.tara.util.CartItems;

public class FoodDescription extends AppCompatActivity {
    private String hotelId, tableId;
    private FoodMenuModel foodItem;
    private ImageView foodImage;
    private TextView foodName, foodDesc, foodPrice, rating;
    private ProgressBar progressBar;
    private Button addToCart;
    private CartItems cartDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_description);

        setTitle("Food Description");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodImage = (ImageView) findViewById(R.id.food_image);
        foodName = (TextView) findViewById(R.id.food_title);
        foodDesc = (TextView) findViewById(R.id.food_desc);
        foodPrice = (TextView) findViewById(R.id.food_price);
        rating = (TextView) findViewById(R.id.rating);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        addToCart = (Button) findViewById(R.id.add_to_cart);
        cartDatabase = new CartItems(this);

        Intent data = getIntent();
        if (data != null) {
            hotelId = data.getStringExtra("hotelId");
            tableId = data.getStringExtra("tableId");
            foodItem = (FoodMenuModel) data.getSerializableExtra("foodItemModel");
            System.out.println("FOOD ITEM::::::::::::::" + foodItem.getName());
        }

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDatabase.addFood(foodItem.getFoodId(), foodItem.getName(), foodItem.getDesc(),
                        (int) foodItem.getPrice(), foodItem.getImageUrl());
                Toast.makeText(getApplicationContext(), foodItem.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            }
        });
        populateUI();
    }

    private void populateUI() {
        FirebaseStorage.getInstance().getReference().child(foodItem.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext())
                        .load(uri)
                        .fit()
                        .centerInside()
                        .into(foodImage);
            }
        });
        foodName.setText(foodItem.getName());
        foodDesc.setText(foodItem.getDesc());
        foodPrice.setText(foodItem.getPrice() + " \u20B9");
        progressBar.setProgress(foodItem.getAvgStars());
        rating.setText(foodItem.getAvgStars() + "");
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
        } else if (id == R.id.action_signin) {
            Intent intent = new Intent(getApplicationContext(), Cart.class);
            intent.putExtra("hotelId", hotelId);
            intent.putExtra("tableId", tableId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

