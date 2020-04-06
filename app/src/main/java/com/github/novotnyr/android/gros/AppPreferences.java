package com.github.novotnyr.android.gros;

import android.content.*;

import java.util.Date;

public class AppPreferences {
    public static final String LAST_PAYMENT = "last-payment";

    private SharedPreferences preferences;

    public AppPreferences(Context context) {
        String preferenceName = context.getPackageName() + ".gros";
        preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    public void store() {
        preferences.edit()
                .putLong(LAST_PAYMENT, new Date().getTime())
                .apply();
    }

    public Date get() {
        long lastPayment = preferences.getLong(LAST_PAYMENT, 0);
        if (lastPayment == 0) {
            return null;
        }
        return new Date(lastPayment);
    }
}
