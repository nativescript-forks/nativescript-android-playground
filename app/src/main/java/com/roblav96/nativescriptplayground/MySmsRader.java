package com.roblav96.nativescriptplayground;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

/**
 * Created by roblav96 on 10/25/16.
 */



public class MySmsRader {
    private static final String TAG = "roblav96";



    private Context _context;

    public MySmsRader(
            Context context
    ) {
        _context = context;

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

    public void stopService() {
        SmsRadar.stopSmsRadarService(_context);
    }

    private void onSms(Sms sms) {
        RequestQueue queue = Volley.newRequestQueue(this);
    }





}


































//
