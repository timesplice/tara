package com.tara.tara;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tara.tara.fragments.FingerprintAuth;

public class ChoosePayment extends AppCompatActivity {

    private LinearLayout footer;
    private RadioGroup paymenTypeGroup;
    private RadioButton paymentType;
    private String hotelId, tableId;
    private int total = 0;
    private TextView foodTotal, grandTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);

        setTitle("Choose Payment");
        Intent data = getIntent();
        if (data != null) {
            hotelId = data.getStringExtra("hotelId");
            tableId = data.getStringExtra("tableId");
            total = data.getIntExtra("total", 0);
        }

        paymenTypeGroup = (RadioGroup) findViewById(R.id.payment_radio_group);
        foodTotal = (TextView) findViewById(R.id.prod_summary_price);
        foodTotal.setText(total + "");
        grandTotal = (TextView) findViewById(R.id.price);
        grandTotal.setText(total + "");
        footer = (LinearLayout) findViewById(R.id.footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = paymenTypeGroup.getCheckedRadioButtonId();
                paymentType = (RadioButton) findViewById(selectedId);
                makePayment();
            }
        });
    }

    private void makePayment() {
        FingerprintAuth fingerprintDialog = new FingerprintAuth();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fingerprintDialog.show(fragmentManager, " ");
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_cod:
                if (checked) {
                    paymenTypeGroup.getCheckedRadioButtonId();
                    break;
                }
            case R.id.radio_debit:
                if (checked) {
                    paymenTypeGroup.getCheckedRadioButtonId();
                    break;
                }
            case R.id.radio_netbanking:
                if (checked) {
                    paymenTypeGroup.getCheckedRadioButtonId();
                    break;
                }
            case R.id.radio_wallet:
                if (checked) {
                    paymenTypeGroup.getCheckedRadioButtonId();
                    break;
                }
        }
    }
}
