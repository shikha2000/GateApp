package com.example.excelschool.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.excelschool.Util.HTTPService;

public class SMSReciever extends BroadcastReceiver {
    private static final String TAG = SMSReciever.class.getSimpleName();
    public static final String SMS_ORIGIN = "ExcelH";
    public static String OTP_DELIMITER = ":";
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";


    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        Log.e(TAG, "Received SMS:====== "+bundle);
        //Toast.makeText(context,"SMS Received",Toast.LENGTH_LONG).show();

        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.d(TAG, "Received SMS: ====" + message + ", Sender: ===" + senderAddress);
                    //  Toast.makeText(context,"Received SMS: " + message + ", Sender: " + senderAddress,Toast.LENGTH_LONG).show();

                    if (!senderAddress.toLowerCase().contains(SMS_ORIGIN.toLowerCase())) {
                        return;
                    }

                    String verificationCode = getVerificationCode(message);
                    Log.d (TAG,"verificationCode ====: "+verificationCode);
                    //   Toast.makeText(context,"code="+verificationCode,Toast.LENGTH_LONG).show();

                    Intent hhtpIntent = new Intent(context, HTTPService.class);
                    hhtpIntent.putExtra("otp", verificationCode);
                    context.startService(hhtpIntent);
                }
            }

        }catch(Exception e){
            Log.e("sms", "Exception: " + e.getMessage());
        }
    }

    private String getVerificationCode(String message) {

        String code = null;
        int index = message.indexOf(OTP_DELIMITER);

        if (index != -1) {
            int start = index+2;
            int length = 4;
            code = message.substring(start, start + length);
            return code;
        }

        return code;
    }
}
