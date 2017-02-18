package com.tara.tara.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;

import com.tara.tara.fragments.FingerprintAuth;


/**
 * Created by M1032467 on 11/8/2016.
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintAuthHelper extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal cancellationSignal;
    private FingerprintAuth purchase;
    private final FingerprintManager mFingerprintManager;
    boolean selfCancelled;

    public FingerprintAuthHelper(FingerprintAuth context, FingerprintManager mFingerprintManager) {
        purchase = context;
        this.mFingerprintManager = mFingerprintManager;
    }

    public void startListening(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(purchase.getContext(),
                Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    public void stopListening() {
        if (cancellationSignal != null) {
            selfCancelled = true;
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {

    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        purchase.onAuthFailed(helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        purchase.onAuthFailed(null);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        purchase.onAuthSuccess();
    }
}
