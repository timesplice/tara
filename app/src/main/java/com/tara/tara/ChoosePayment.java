package com.tara.tara;

import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tara.tara.fragments.FingerprintAuth;

public class ChoosePayment extends AppCompatActivity {

    private LinearLayout footer;
    private RadioGroup paymenTypeGroup;
    private RadioButton paymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);

        paymenTypeGroup = (RadioGroup) findViewById(R.id.payment_radio_group);
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
