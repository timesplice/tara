package com.tara.tara;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tara.tara.fragments.FingerprintAuth;

public class HotelAnimation extends AppCompatActivity {

    private Animation animZoomIn, animZoomOut;
    private ImageView hotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_animation);

        getSupportActionBar().setElevation(0);
        setTitle(" ");
        hotel = (ImageView) findViewById(R.id.hotel_anim);
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
                FingerprintAuth fingerprintDialog = new FingerprintAuth();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fingerprintDialog.show(fragmentManager, " ");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
