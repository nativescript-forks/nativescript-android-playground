package com.roblav96.nativescriptplayground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by roblav96 on 10/25/16.
 */



public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "roblav96";

    public SmsReceiver() {
        super();
    }

    private void receiveMessage(SmsMessage message) {
        Log.e(TAG, "receiveMessage > SmsMessage > " + message);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (SmsMessage message : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                receiveMessage(message);
            }
            return;
        }

        try {
            final Bundle bundle = intent.getExtras();

            if (bundle == null || ! bundle.containsKey("pdus")) {
                return;
            }

            final Object[] pdus = (Object[]) bundle.get("pdus");

            for (Object pdu : pdus) {
                receiveMessage(SmsMessage.createFromPdu((byte[]) pdu));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
