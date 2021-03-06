package com.roblav96.nativescriptplayground;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.tuenti.smsradar.Sms;
import com.tuenti.smsradar.SmsListener;
import com.tuenti.smsradar.SmsRadar;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import bolts.Task;
import io.requery.android.database.sqlite.SQLiteDatabase;
import io.requery.android.database.sqlite.SQLiteStatement;
import me.everything.providers.android.contacts.ContactsProvider;

/**
 * Created by roblav96 on 10/1/16.
 */



public class Threads {
    private static final String TAG = "roblav96";



    public static Task<NotificationCompat.Builder> getNotificationBuilder(
            final Activity activity,
            final int icon,
            final String title,
            final String content,
            final boolean ongoing
    ) {
        return Task.callInBackground(new Callable<NotificationCompat.Builder>() {
            @Override
            public NotificationCompat.Builder call() throws Exception {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);
                builder.setSmallIcon(icon);
                builder.setContentTitle(title);
                builder.setContentText(content);
                builder.setOngoing(ongoing);
                return builder;
            }
        });
    }



    private class InputSQLiteStatement<T> {
        private String query;
        private T[] values;
    }

    public static Task<Boolean> sqlWriteAsync(
            final SQLiteDatabase db,
            final String inputStatements
    ) {
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Gson gson = new Gson();
//                ArrayList<SQLiteStatement> statements = gson.fromJson(_statements, ArrayList<SQLiteStatement.class>);

//                Log.e(TAG, "db > " + gson.toJson(db));
//                Log.e(TAG, "inputStatements > " + inputStatements);

                InputSQLiteStatement<?>[] statements = gson.fromJson(inputStatements, InputSQLiteStatement[].class);
//                Log.e(TAG, "statements.length > " + statements.length);

                db.beginTransactionNonExclusive();

                for (InputSQLiteStatement statement : statements) {
//                    Log.e(TAG, "statement > " + gson.toJson(statement));
                    SQLiteStatement compiled = db.compileStatement(statement.query);
                    for (int i = 0; i < statement.values.length; i++) {
                        if (statement.values[i] instanceof String) {
                            compiled.bindString(i + 1, statement.values[i].toString());
//                        } else if (statement.values[i] instanceof Long) {
//                            compiled.bindLong(i + 1, Long.parseLong(statement.values[i].toString()));
                        } else if (statement.values[i] instanceof Double) {
                            compiled.bindDouble(i + 1, Double.parseDouble(statement.values[i].toString()));
                        } else {
                            compiled.bindNull(i + 1);
                        }
                    }
                    compiled.execute();
                    compiled.clearBindings();
                }

                db.setTransactionSuccessful();
                db.endTransaction();

                return true;
            }
        });
    }

    public static Task<Cursor[]> sqlReadAsync(
            final SQLiteDatabase db,
            final String inputStatements
    ) {
        return Task.callInBackground(new Callable<Cursor[]>() {
            @Override
            public Cursor[] call() throws Exception {
                Gson gson = new Gson();
                InputSQLiteStatement<?>[] statements = gson.fromJson(inputStatements, InputSQLiteStatement[].class);

                ArrayList<Cursor> cursors = new ArrayList<>();
                for (InputSQLiteStatement statement : statements) {
                    cursors.add(db.rawQuery(statement.query, statement.values));
                }

                return cursors.toArray(new Cursor[0]);
            }
        });
    }



    public static Task<String> getContentProviderAsync(
            final Context context,
            final Uri uri,
            final @Nullable String[] projection,
            final @Nullable String selection,
            final @Nullable String[] selectionArgs,
            final @Nullable String sortOrder,
            final @Nullable boolean first
    ) {
        return Task.callInBackground(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Cursor cursor = context.getContentResolver().query(
                        uri,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder
                );

                ArrayList<ArrayList<String>> sendMe = new ArrayList<>();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        ArrayList<String> item = new ArrayList<>();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            item.add(cursor.getColumnName(i));
                            item.add(cursor.getString(i));
                        }
                        sendMe.add(item);
                        if (first) {
                            break;
                        }
                    }
                    cursor.close();
                }

                // String sendi = new Gson().toJson(sendMe);
                // Log.e(TAG, "sendi > " + sendi);
                // return sendi;
                return new Gson().toJson(sendMe);
            }
        });
    }



    public static Task<String> getAllContactsAsync(
            final Context context
    ) {
        return Task.callInBackground(new Callable<String>() {
            @Override
            public String call() throws Exception {
                ContactsProvider contactsProvider = new ContactsProvider(context);
                // String sendi = new Gson().toJson(contactsProvider.getContacts().getList());
                // Log.e(TAG, "sendi > " + sendi);
                // return sendi;
                return new Gson().toJson(contactsProvider.getContacts().getList());
            }
        });
    }



}

































//
