package com.github.novotnyr.android.gros;

import android.os.*;
import android.telephony.SmsManager;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.*;

import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.SEND_SMS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {
    private Button payButton;

    private static final int REQUEST_SMS = 1;

    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        payButton = findViewById(R.id.payButton);

        appPreferences = new AppPreferences(this);
    }

    public void onPayButtonClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, SEND_SMS) != PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, SEND_SMS)) {
                Snackbar.make(view, "Platba sa vykonáva odosielaním SMS práv. Udeľte appke právo odosielať SMSky", Snackbar.LENGTH_LONG).show();
            }
            String[] permissions = {SEND_SMS};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_SMS);
        } else {
            sendSms();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SMS:
                if (grantResults.length == 0 || grantResults[0] != PERMISSION_GRANTED ) {
                    // Neboli udelené žiadne oprávnenia, alebo dialóg pre potvrdenie bol zrušený
                    return;
                }
                sendSms();
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void sendSms() {
        String phoneNumber = "5556";
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber,
                null, "KE-123AB A4", null, null);
        appPreferences.store();
        refreshButton();
    }

    private void refreshButton() {
        Date date = appPreferences.get(); //<1>
        if (date == null) {
            return;
        }
        CharSequence period = DateUtils.getRelativeTimeSpanString(date.getTime(), new Date().getTime(), DateUtils.SECOND_IN_MILLIS); //<2>
        this.payButton.setText(period); //<3>
    }
}
