package com.roblav96.nativescriptplayground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by roblav96 on 10/25/16.
 */



public class Sms {
    private static final String TAG = "roblav96";

    private BroadcastReceiver receiver;
    private boolean registered = false;

    public Sms(Context context) {
        receiver = new SmsReceiver();
        registerReceiver(context, receiver);
    }

    private void registerReceiver(Context context, BroadcastReceiver receiver) {
        if (registered) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && context != null) {
            context.registerReceiver(
                    receiver,
                    new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
            );
            registered = true;
            return;
        }

        if (context != null) {
            context.registerReceiver(
                    receiver,
                    new IntentFilter("android.provider.Telephony.SMS_RECEIVED")
            );
            registered = true;
        }
    }

    private void unregisterReceiver(Context context) {
        if (registered && context != null) {
            context.unregisterReceiver(receiver);
            registered = false;
        }
    }

}
