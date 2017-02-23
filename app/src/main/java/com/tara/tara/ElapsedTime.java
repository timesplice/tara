package com.tara.tara;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tara.tara.fragments.HotelServicesList;
import com.tara.tara.model.FoodMenuModel;
import com.tara.tara.model.HotelOrderModel;
import com.tara.tara.model.UserOrderModel;
import com.tara.tara.util.CartItems;
import com.tara.tara.util.UserPreference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.tara.tara.Login.fDatabase;

public class ElapsedTime extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView timerText, headingText;
    private String hotelId, tableId, userId, orderId;
    private UserPreference userPreference;
    private CartItems cartItemsDb;
    private boolean timer = false;
    private Long givenTimeMillis;
    private int estimatedTime;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elapsed_time);

        setTitle("Elapsed Time");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        timerText = (TextView) findViewById(R.id.timer);
        headingText = (TextView) findViewById(R.id.heading);
        userPreference = new UserPreference(this);
        cartItemsDb = new CartItems(this);

        Intent data = getIntent();
        if (data != null) {
            hotelId = data.getStringExtra("hotelId");
            tableId = data.getStringExtra("tableId");
            total = data.getIntExtra("total", 0);
        }
        userId = userPreference.getUserDetails().getUserId();

        addHotelOrder();
        //addUserOrder();
        userOrderChangeListener();

    }

    public void addHotelOrder() {
        //addspinner
        DatabaseReference fDBReference = fDatabase.getReference("hotelOrders").child(hotelId);
        String orderKey = fDBReference.push().getKey();
        Map<String, Integer> orderedItems = new HashMap<String, Integer>();
        Cursor cursor = cartItemsDb.getAllItems();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                orderedItems.put(cursor.getString(cursor.getColumnIndex(Cart.FOOD_ID)), 1);
            } while (cursor.moveToNext());
        }
        HotelOrderModel hotelOrder = new HotelOrderModel(userId, hotelId, tableId, orderKey, orderedItems);

        fDBReference.child(orderKey).setValue(hotelOrder);
        System.out.println("Added Hotel Order");

        orderId = orderKey;
    }

    public void addUserOrder() {
        DatabaseReference fDBReference = fDatabase.getReference("userOrders").child(userId);
        UserOrderModel userOrder = new UserOrderModel(hotelId, tableId, userId, orderId);
        fDBReference.child(orderId).setValue(userOrder);
        System.out.println("Add User Order");
    }

    public void userOrderChangeListener() {
        DatabaseReference fDBReference = fDatabase.getReference("userOrders").child(userId);
        fDBReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserOrderModel userOrder = dataSnapshot.getValue(UserOrderModel.class);
                Log.d("ADDED:", "User order by:" + userOrder.hotel + "; orderID:" + dataSnapshot.getKey());
                givenTimeMillis = userOrder.getTimeStamp();
                //givenTimeMillis = new Date().getTime();
                estimatedTime = (int) userOrder.getWaitingTime() * 60;
                timer = true;
                progressBar.setMax((int) estimatedTime);
                startTimer();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                showNotif();
                UserOrderModel userOrder = dataSnapshot.getValue(UserOrderModel.class);
                Log.d("CHANGED:", "User order by:" + userOrder.hotel + "; orderID:" + dataSnapshot.getKey());
                if (userOrder.getDelivered() == true) {
                    timer = false;
                    Intent intent = new Intent(ElapsedTime.this, ChoosePayment.class);
                    intent.putExtra("total", total);
                    intent.putExtra("orderId", dataSnapshot.getKey());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("REMOVED:", "User Order Removed from Hotel");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("MOVED:", "User Order Moved from Hotel");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("CANCEL:", "User Order Canceled from Hotel");
            }
        });
    }

    private void showNotif() {
        Intent intent = new Intent(getApplicationContext(), HotelServicesList.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Coming soon");
        bigTextStyle.bigText("Your food is almost ready and about to reach you from the chef !");
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_chef)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_chef))
                .setStyle(bigTextStyle)
                .setColor(getResources().getColor(R.color.blue_grey))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    Runnable mRunnable;

    private void startTimer() {
        final Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                long seconds = (System.currentTimeMillis() - givenTimeMillis) / 1000;
                String text = getDurationText(seconds);
                timerText.setText(getDurationText(seconds));
                progressBar.setProgress((int) seconds);
                if (seconds < estimatedTime) {
                    timerText.setTextColor(Color.BLACK);
                } else {
                    timerText.setTextColor(Color.RED);
                }
                System.out.println("TIMER:::");

                if (timer == true)
                    mHandler.postDelayed(mRunnable, 1000L);
            }
        };
        mHandler.postDelayed(mRunnable, 0L);
    }

    private String getDurationText(long sec) {
        long min = sec / 60;
        return String.format("%02d:%02d:%02d", min / 60, min % 60, sec % 60);
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
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
