package com.roblav96.nativescriptplayground;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.util.Log;

import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;
import com.tuenti.smsradar.SmsRadarService;

/**
 * Created by roblav96 on 10/25/16.
 */



public class MySmsRader {
    private static final String TAG = "roblav96";



    boolean started = false;

    void startSmsRadar(
            final Context context
    ) {
        if (started) {
            return;
        }

        SmsRadar.initializeSmsRadarService(context, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {
                Log.e(TAG, "onSmsSent > sms > " + sms);
                onSms(sms);
            }
            @Override
            public void onSmsReceived(Sms sms) {
                Log.e(TAG, "onSmsReceived > sms > " + sms);
                onSms(sms);
            }
        });
    }

    void stopSmsRadar(
            final Context context
    ) {
        if (!started) {
            return;
        }
        SmsRadar.stopSmsRadarService(context);
    }

    private void onSms(Sms sms) {
        RequestQueue queue = Volley.newRequestQueue(this)
    }





}


































//
