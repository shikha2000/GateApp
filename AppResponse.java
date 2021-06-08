package com.example.excelschool.interfaces;

public interface AppResponse {

    void onResponse(String response, String responseType);

     void onError(String error, String responseType);


}
