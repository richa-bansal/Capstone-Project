package com.village.wannajoin.FCMNotificationData;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.village.wannajoin.R;

/**
 * Created by richa on 9/17/16.
 */
public class NotificationQueryHandler extends AsyncQueryHandler {
    Context mContext;
    public static final String LOG_TAG = NotificationQueryHandler.class.getSimpleName();
    public NotificationQueryHandler(ContentResolver cr, Context context) {
        super(cr);
        mContext = context;
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        super.onInsertComplete(token, cookie, uri);
        if (uri != null)
            //Toast.makeText(mContext, R.string.location_saved_string,Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "Row inserted successfully.");
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        super.onDeleteComplete(token, cookie, result);
        if (result>0)
            Log.d(LOG_TAG, "Row deleted successfully.");
    }


}