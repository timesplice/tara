package com.tara.tara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.tara.tara.fragments.FingerprintAuth;

public class HotelAnimation extends AppCompatActivity {

    private Animation animZoomIn, animZoomOut;
    private TextView hotel;
    String hotelId, hotelName, tableId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_animation);

        getSupportActionBar().setElevation(0);
        setTitle(" ");
        hotel = (TextView) findViewById(R.id.hotel_anim);
        Intent hotelDetails = getIntent();
        if (hotelDetails != null) {
            hotelName = hotelDetails.getStringExtra("hotelName");
            hotelId = hotelDetails.getStringExtra("hotelId");
            tableId = hotelDetails.getStringExtra("tableId");
        }
        hotel.setText(hotelName.replace(" ", "\n"));
        animateHotel();
    }

    private void animateHotel() {
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_zoomin);
        animZoomIn.setStartOffset(500);
        hotel.startAnimation(animZoomIn);
        animZoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                zoomOutHotel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void zoomOutHotel() {
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_zoomout);
        hotel.startAnimation(animZoomIn);
        animZoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent category = new Intent(HotelAnimation.this, FoodCategory.class);
                category.putExtra("tableId", tableId);
                category.putExtra("hotelId", hotelId);
                startActivity(category);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
