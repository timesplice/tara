package com.tara.tara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tara.tara.model.HotelModel;
import com.tara.tara.util.ScanPreference;

import org.json.JSONException;
import org.json.JSONObject;

public class StartScan extends AppCompatActivity {

    private Button scan;
    private IntentIntegrator qrScan;
    private TextView previousMenu;
    private ScanPreference scanPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_scan);

        setTitle("Scan");
        scan = (Button) findViewById(R.id.scan);
        previousMenu = (TextView) findViewById(R.id.previous_order);
        qrScan = new IntentIntegrator(this);
        scanPreference = new ScanPreference(this);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });
        previousMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanPreference.setScanInPreference("hotel1", "table1");
                Intent hotelDetails = new Intent(StartScan.this, HotelAnimation.class);
                hotelDetails.putExtra("hotelName", "Alto Vino");
                startActivity(hotelDetails);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    System.out.println("RESULT:::::::::::::::::::" + result.getContents());
                    final String hotelId = (String) obj.get("hotelId");
                    final String tableId = (String) obj.get("tableId");
                    if (hotelId != null && tableId != null) {
                        Login.fDatabase.getReference("hotels/" + hotelId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null && dataSnapshot.getKey() != null) {
                                    HotelModel hotelModel = dataSnapshot.getValue(HotelModel.class);

                                    scanPreference.setScanInPreference(hotelId, tableId);
                                    System.out.println("Scan" + hotelId + tableId);
                                    Intent hotelDetails = new Intent(StartScan.this, HotelAnimation.class);
                                    hotelDetails.putExtra("hotelName", hotelModel.getName());
                                    startActivity(hotelDetails);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(this, "Invalid QR code scanned", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
