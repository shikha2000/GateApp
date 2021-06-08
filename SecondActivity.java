package com.example.excelschool.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.excelschool.R;
import com.example.excelschool.interfaces.AppResponse;
import com.example.excelschool.network.ApiCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AppResponse {

    private static final String TAG =SecondActivity.class.getSimpleName();
    private ImageView imgBack;
    private TextView tvAdd,tvCode;
    private EditText etName,etNumber;
    private Button BtnGo;
    private String centreName,centreReason;
    String number,centreId,reasonId;
    private Spinner SpinnerReason;
    private static final String OTP = "https://www.excelplayschool.com/api/logic-test.php?";

    String[] reason = { "Select Reason For Visit","Admission",
            "Interview",
            "Vendor",
            "Other"};
    private ProgressDialog progressDialog;
    private ApiCall apiCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        apiCall=new ApiCall(this,this);
        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Loading...");


        getID();


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        centreName = intent.getStringExtra("centre_name");
        if (centreName.equals("Sector 122 Noida, U.P")){
            centreId = "1";
        }
        if (centreName.equals("Sector 116 Noida, U.P")){
            centreId = "2";
        }
        if (centreName.equals("Sector 104 Noida, U.P")){
            centreId = "3";
        }
        if (centreName.equals("Dilshad Garden, Delhi")){
            centreId = "4";
        }

        tvAdd.setText(centreName);


        SpinnerReason.setOnItemSelectedListener(this);
        BtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=etName.getText().toString().trim();
                 number=etNumber.getText().toString().trim();
                 if (name.isEmpty()||name.equals("null")){
                     Toast.makeText(SecondActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                     return;
                 }
                if (TextUtils.isEmpty(number)){
                    Toast.makeText(SecondActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (centreReason.equals("Select Reason For Visit")){
                    Toast.makeText(SecondActivity.this, "Please select reason for visit", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (centreReason.equals("Admission")){
                    reasonId= "1";
                }
                if (centreReason.equals("Interview")){
                    reasonId = "2";
                }
                if (centreReason.equals("Vendor")){
                    reasonId = "3";
                }
                if (centreReason.equals("Other")){
                    reasonId = "4";
                }


                /*Intent i = new Intent(SecondActivity.this,ThirdActivity.class);
                i.putExtra("centre_reason",centreReason);
                startActivity(i);*/

                HashMap<String,String> params = new HashMap<>();
                Log.d(TAG, "onClick: "+centreId);
                params.put("action","generateOtp");
                params.put("center_id",centreId);
                params.put("name",name);
                params.put("mobile",number);
                params.put("reason",centreReason);
                progressDialog.show();
                apiCall.sendData(Request.Method.POST,OTP,params,"sendOtp");


            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,reason);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerReason.setAdapter(arrayAdapter);
        SpinnerReason.setGravity(Gravity.CENTER);

    }

    private void getID() {
        imgBack=findViewById(R.id.imgBack);
        tvAdd=findViewById(R.id.tvAdd);
        tvCode=findViewById(R.id.tvCode);
        etName=findViewById(R.id.etName);
        etNumber=findViewById(R.id.etNumber);
        BtnGo=findViewById(R.id.BtnGo);
        SpinnerReason=findViewById(R.id.SpinnerReason);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: "+reason[position]);
        Log.d(TAG, "onItemSelected: "+reason[(int) id]);
        centreReason = reason[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResponse(String response, String responseType) {
        progressDialog.dismiss();
        Log.d(TAG, "onResponse: "+response);
        try {
            JSONObject jsonObject= new JSONObject(response);
            int responseCode = jsonObject.getInt("status");
            if (responseCode==1){
               Intent intent= new Intent(SecondActivity.this,ThirdActivity.class);
                intent.putExtra("centre_name",centreName);
                intent.putExtra("centre_reason",centreReason);
                intent.putExtra("centre_id",centreId);
                intent.putExtra("mobile",number);
                //intent.putExtra("centre_reason",centreReason);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(String error, String responseType) {

        progressDialog.dismiss();
    }
}
