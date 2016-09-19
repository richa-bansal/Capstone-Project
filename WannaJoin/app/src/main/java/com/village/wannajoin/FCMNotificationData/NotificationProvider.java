package com.village.wannajoin.FCMNotificationData;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


/**
 * Created by richa on 9/17/16.
 */
public class NotificationProvider extends ContentProvider {

    private NotificationDbHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static final int NOTIFICATION = 200;

    @Override
    public boolean onCreate() {
        mOpenHelper = new NotificationDbHelper(getContext());
        return true;
    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NotificationContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, NotificationContract.PATH_NOTIFICATIONS, NOTIFICATION);

        return matcher;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case NOTIFICATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        NotificationContract.NotificationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTIFICATION:
                return NotificationContract.NotificationEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        if (match ==NOTIFICATION){
            //check if the row exists already
            String selection = NotificationContract.NotificationEntry.COLUMN_NOTIFICATION_ID + "="+ "\""+values.getAsString(NotificationContract.NotificationEntry.COLUMN_NOTIFICATION_ID)+"\"";
            String[] NOTIFICATION_COLUMNS = {
                    NotificationContract.NotificationEntry.TABLE_NAME + "." + NotificationContract.NotificationEntry._ID,

            };
            Cursor cur = db.query(NotificationContract.NotificationEntry.TABLE_NAME,NOTIFICATION_COLUMNS,selection,null,null,null,null);

            if (cur.getCount()>0)
                return null;

            //else continue inserting the row

            long _id = db.insert(NotificationContract.NotificationEntry.TABLE_NAME,null,values);
            if ( _id > 0 )
                returnUri = NotificationContract.NotificationEntry.buildNotificationsUri(_id);
            else
                throw new android.database.SQLException("Failed to insert row into " + uri);
        }else
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case NOTIFICATION:
                rowsDeleted = db.delete(
                        NotificationContract.NotificationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case NOTIFICATION:

                rowsUpdated = db.update(NotificationContract.NotificationEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
