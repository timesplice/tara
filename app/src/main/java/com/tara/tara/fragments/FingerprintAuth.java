package com.tara.tara.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tara.tara.R;
import com.tara.tara.ThankPage;
import com.tara.tara.util.FingerprintAuthHelper;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FingerprintAuth extends DialogFragment {

    private static final String KEY_NAME = "fingerprint_key";
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;

    private ImageView fingerprintPlaceholder;
    private Animation animFadeIn;
    private TextView status;
    private Button cancel;
    private Long productId = Long.valueOf(0);
    private String productName, productPrice, productDesc, productImageurl;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_fingerprint_auth, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = bundle.getLong("product_id", 0);
            productName = bundle.getString("product_name");
            productPrice = bundle.getString("product_price");
            productImageurl = bundle.getString("product_imageurl");
            productDesc = bundle.getString("product_desc");
        }

        keyguardManager = (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getActivity().getSystemService(FINGERPRINT_SERVICE);
        status = (TextView) view.findViewById(R.id.fingerprint_status);
        fingerprintPlaceholder = (ImageView) view.findViewById(R.id.fingerprint_placeholder);
        fingerprintPlaceholder.setImageResource(R.drawable.ic_fingerprint);
        cancel = (Button) view.findViewById(R.id.second_dialog_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Fingerprint authentication permission not enabled",
                        Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
            if (!fingerprintManager.isHardwareDetected()) {
                // Redirect directly to checkout
                Toast.makeText(getContext(), "No hardware detected", Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
            if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(getContext(),
                        "Lock screen security not enabled in Settings",
                        Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                // This happens when no fingerprints are registered.
                Toast.makeText(getContext(),
                        "Register at least one fingerprint in Settings",
                        Toast.LENGTH_LONG).show();
                getDialog().dismiss();
                ;
            }

            generateKey();

            if (cipherInit()) {
                cryptoObject = new FingerprintManager.CryptoObject(cipher);
                FingerprintAuthHelper helper = new FingerprintAuthHelper(this, fingerprintManager);
                helper.startListening(fingerprintManager, cryptoObject);
            }
        }
        return view;
    }


    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
        } catch (NoSuchAlgorithmException |
                NoSuchProviderException e) {
            throw new RuntimeException(
                    "Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public void onAuthSuccess() {
        fingerprintPlaceholder.setImageResource(R.drawable.ic_success);
        status.setTextColor(ContextCompat.getColor(getContext(), R.color.success_color));
        animFadeIn = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_in);
        animFadeIn.setStartOffset(300);
        fingerprintPlaceholder.startAnimation(animFadeIn);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                status.setText(getString(R.string.auth_success));
                startActivity(new Intent(getContext(), ThankPage.class));
                getDialog().dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void onAuthFailed(final CharSequence message) {
        fingerprintPlaceholder.setImageResource(R.drawable.ic_warning);
        status.setTextColor(ContextCompat.getColor(getContext(), R.color.warning_color));
        animFadeIn = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.fade_in);
        animFadeIn.setStartOffset(300);
        fingerprintPlaceholder.startAnimation(animFadeIn);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (message != null)
                    status.setText(message.toString());
                status.setText(getString(R.string.auth_failure));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }}
