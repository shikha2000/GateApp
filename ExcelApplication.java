package com.example.excelschool.appController;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.excelschool.reciever.ConnectivityReciever;

public class ExcelApplication extends Application {
    private static final String TAG = ExcelApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static ExcelApplication mExcelApplication;
    @Override
    public void onCreate() {
        super.onCreate();
//        JodaTimeAndroid.init(this);
        mExcelApplication  = this;
    }
    public static synchronized ExcelApplication getNStayApplication(){
        return mExcelApplication;
    }
    public RequestQueue getRequestQueue(){
        if (mRequestQueue==null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public void setConnectivityListener(ConnectivityReciever.ConnectivityReceiverListener listener) {
        ConnectivityReciever.connectivityReceiverListener = listener;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }
    public void cancelPendingRequests(Object object){
        if (mRequestQueue!=null){
            mRequestQueue.cancelAll(object);
        }
    }
}
