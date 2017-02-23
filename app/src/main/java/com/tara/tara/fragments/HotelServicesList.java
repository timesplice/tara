package com.tara.tara.fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.tara.tara.Cart;
import com.tara.tara.FoodCategory;
import com.tara.tara.R;
import com.tara.tara.model.HotelOrderModel;

import java.util.HashMap;
import java.util.Map;

import static com.tara.tara.Login.fDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelServicesList extends Fragment {

    private TextView viewMenu;
    private TextView contactLaundary;

    public HotelServicesList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hotel_services_list, container, false);

        viewMenu = (TextView) view.findViewById(R.id.view_menu);
        contactLaundary = (TextView) view.findViewById(R.id.laundry);
        viewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FoodCategory.class));
            }
        });

        contactLaundary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return view;
    }

    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setCancelable(true);
        // set dialog message
        alertDialogBuilder.setTitle("Confirmation");
        alertDialogBuilder.setMessage("Do you really want ot send a request to our laundry service ?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sendRequest();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        // show it
        alertDialog.show();
    }

    private void sendRequest() {
        DatabaseReference fDBReference = fDatabase.getReference("hotelOrders").child("laundry");
        String orderKey = fDBReference.push().getKey();
        Map<String, Integer> orderedItems = new HashMap<String, Integer>();


        HotelOrderModel hotelOrder = new HotelOrderModel("laundryUser", "laundry", "laundryTable", orderKey, orderedItems);

        fDBReference.child(orderKey).setValue(hotelOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                showNotification();
            }
        });
        System.out.println("Added Hotel Order");

    }

    private void showNotification() {
        Intent intent = new Intent(getContext(), HotelServicesList.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        NotificationCompat.Builder b = new NotificationCompat.Builder(getContext());
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle("Laundry request");
        bigTextStyle.bigText("Your request for laundry order has been sent");
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_chef)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_chef))
                .setStyle(bigTextStyle)
                .setColor(getResources().getColor(R.color.blue_grey))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }
}
