package com.example.excelschool.Util;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class HTTPService extends IntentService {
    private static String TAG = HTTPService.class.getSimpleName();

    public HTTPService() {
        super(HTTPService.class.getSimpleName());
    }
    public static final String ACTION_MyIntentService = "com.digital_360.catchme.activity.RESPONSE";


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: ====HttpService");
        if (intent != null) {
            String otp = intent.getStringExtra("otp");
            Intent intentUpdate = new Intent();
            intentUpdate.setAction(ACTION_MyIntentService);
            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
            intentUpdate.putExtra("OTP", otp);

            sendBroadcast(intentUpdate);

        }
    }
}
