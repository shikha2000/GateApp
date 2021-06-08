package com.example.excelschool.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.excelschool.appController.ExcelApplication;
import com.example.excelschool.interfaces.AppResponse;

import java.util.HashMap;
import java.util.Map;

public class ApiCall {
    private static final String TAG  = ApiCall.class.getSimpleName();
    private AppResponse appResponse;
    private Context context;

    public ApiCall(AppResponse appResponse) {
        this.appResponse = appResponse;
    }

    public ApiCall(AppResponse appResponse, Context context) {
        this.appResponse = appResponse;
        this.context = context;
    }

    public void sendData(int requestMethod, final String url, final HashMap<String ,String> data, final String responseType){
        StringRequest stringRequest = new StringRequest(requestMethod, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: "+response+"==="+data+"==="+url+"==="+responseType);
                appResponse.onResponse(response,responseType);
            }
        }, new
                Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: ==="+error);
                        appResponse.onError(error.toString(),responseType);

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "getParams: "+data);
                return data;
            }

            @Override
            public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
                retryPolicy = new DefaultRetryPolicy(1000*10,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return super.setRetryPolicy(retryPolicy);
            }
        };
        ExcelApplication.getNStayApplication().addToRequestQueue(stringRequest);
    }
}
