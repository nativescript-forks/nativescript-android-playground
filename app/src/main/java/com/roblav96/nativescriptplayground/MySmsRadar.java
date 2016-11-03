package com.roblav96.nativescriptplayground;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by roblav96 on 10/25/16.
 */



public class MySmsRadar {
    private static final String TAG = "SANDBOX";



    private String _url;
    private ArrayList<String> _failed;
    private String _headers;
    private ArrayList<String> _unames;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient _client;

    public MySmsRadar(
            Context context,
            String ip,
            String headers
    ) {
        _url = ip + "/api/android-sms";
        _headers = headers;
        _failed = new ArrayList<>();
        _client = new OkHttpClient();
        _unames = new ArrayList<>();

        SmsRadar.initializeSmsRadarService(context, new SmsListener() {
            @Override
            public void onSmsSent(Sms sms) {
                _sendSms(sms);
            }
            @Override
            public void onSmsReceived(Sms sms) {
                _sendSms(sms);
            }
        });
    }

    public void stopService(Context context) {
        SmsRadar.stopSmsRadarService(context);
    }

    public void setUnames(String[] unames) {
//        Log.d(TAG, "setUnames > unames > " + unames.toString());
        this._unames.clear();
        for (int i = 0; i < unames.length; i++) {
            this._unames.add(unames[i]);
        }
//        Log.d(TAG, "setUnames > this._unames > " + this._unames.toString());
    }

    public void addUname(String uname) {
//        Log.d(TAG, "addUname > uname > " + uname);
        uname = uname.replaceAll("[^0-9]", "");
        uname = "1" + uname.substring(uname.length() - 10);
//        Log.d(TAG, "add > uname > " + uname);
        this._unames.add(uname.toString());
    }

    public void removeUname(String uname) {
        uname = uname.replaceAll("[^0-9]", "");
        uname = "1" + uname.substring(uname.length() - 10);
        for (int i = 0; i < this._unames.size(); i++) {
            if (this._unames.get(i) == uname) {
                this._unames.remove(i);
                break;
            }
        }
    }

    private class SmsItem {
        private String uname;
        private String body;
        private String date;
        private String type;
    }

    private void _sendSms(Sms _sms) {
        SmsItem item = new SmsItem();
        item.uname = _sms.getAddress();
        item.uname = item.uname.replaceAll("[^0-9]", "");
        item.uname = "1" + item.uname.substring(item.uname.length() - 10);
        item.body = _sms.getMsg();
        item.date = _sms.getDate();
        item.type = _sms.getType().toString();

//        Log.d(TAG, "_sendSms > item > " + item.toString());
//        Log.d(TAG, "_sendSms > this._unames > " + this._unames.toString());
//        Log.d(TAG, "_sendSms > contains > " + this._unames.contains(item.uname));

        if (!this._unames.contains(item.uname)) {
            return;
        }

        Gson gson = new Gson();
        final String sms = gson.toJson(item);

        ArrayList<String> sendi = new ArrayList<>();
        for (int i = 0; i < _failed.size(); i++) {
            sendi.add(_failed.get(i));
        }
        sendi.add(sms);

        Request.Builder builder = new Request.Builder();
        builder.url(_url);
        builder.post(RequestBody.create(JSON, gson.toJson(sendi)));

        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> headers = gson.fromJson(_headers, type);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        _client.newCall(builder.build()).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                _failed.add(sms);
            }
            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    _failed.clear();
                } else {
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
