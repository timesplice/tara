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
import com.tara.tara.util.ScanPreference;

public class HotelAnimation extends AppCompatActivity {

    private Animation animZoomIn, animZoomOut;
    private TextView hotel;
    String hotelId, hotelName, tableId;
    private ScanPreference scanPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_animation);

        getSupportActionBar().setElevation(0);
        setTitle(" ");
        hotel = (TextView) findViewById(R.id.hotel_anim);
        scanPreference = new ScanPreference(this);
        Intent hotelDetails = getIntent();
        if (hotelDetails != null) {
            hotelName = hotelDetails.getStringExtra("hotelName");
        }
        hotelId = scanPreference.getScanDetails().getHotelId();
        tableId = scanPreference.getScanDetails().getTableId();
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
                Intent home = new Intent(HotelAnimation.this, Home.class);
                startActivity(home);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
