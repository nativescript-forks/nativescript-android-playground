package com.roblav96.nativescriptplayground;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import bolts.Task;
import io.requery.android.database.sqlite.SQLiteDatabase;
import io.requery.android.database.sqlite.SQLiteStatement;
import me.everything.providers.android.contacts.ContactsProvider;

/**
 * Created by roblav96 on 10/1/16.
 */



class InputSQLiteStatement {
    public String query;
    public String[] values;
}

public class Threads {
    private static final String TAG = "roblav96";



    public static Task<Boolean> sqlWriteAsync(
            final SQLiteDatabase db,
            final InputSQLiteStatement[] statements
            ) {
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
//                Gson gson = new Gson();
//                ArrayList<SQLiteStatement> statements = gson.fromJson(_statements, ArrayList<SQLiteStatement.class>);

                db.beginTransactionNonExclusive();

                for (int i = 0; i < statements.length; i++) {
                    InputSQLiteStatement statement = statements[i];
                    SQLiteStatement compiled = db.compileStatement(statement.query);
                    for (int ii = 0; ii < statement.values.length; ii++) {
                        compiled.bindString(ii+1,statement.values[ii]);
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
