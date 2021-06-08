package com.example.excelschool.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.excelschool.R;
import com.example.excelschool.interfaces.AppResponse;
import com.example.excelschool.network.ApiCall;
import com.example.excelschool.pref.ExcelPref;
import com.example.excelschool.reciever.ConnectivityReciever;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ThirdActivity extends AppCompatActivity implements AppResponse {
    private static final String TAG = ThirdActivity.class.getSimpleName();
    private ImageView imgArrowBack, iv_ok;
    private TextView tv_Add, tv_otpNum, tv_otpValidate, tv_resend, tv_changeNum;
    private LinearLayout ll_otpBox;
    private EditText OTP_ET_1, OTP_ET_2, OTP_ET_3, OTP_ET_4;
    private Button btnValidate,btnok;
    private ApiCall apiCall;
    String number;
    String id;
    private String centreName,centreId;
    private RelativeLayout RelativeOtp,RelativeOk;
    private static final String OTP_VERIFY = "https://www.excelplayschool.com/api/logic-test.php?";

    String otp;
    private static final int READ_SMS_PERMISSION_CODE = 23;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        getId();

        imgArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent i = getIntent();

        centreName = i.getStringExtra("centre_name");
        centreId = i.getStringExtra("centre_id");


        tv_Add.setText(centreName);

        //stayPref = new NStayPref(this);
        apiCall = new ApiCall(this, this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        Intent intent = getIntent();
        number = intent.getStringExtra("mobile");
        tv_otpNum.setText("+91 " + number);
        //request permission
        OTP_ET_1.addTextChangedListener(new GenericTextWatcher(OTP_ET_1));
        OTP_ET_2.addTextChangedListener(new GenericTextWatcher(OTP_ET_2));
        OTP_ET_3.addTextChangedListener(new GenericTextWatcher(OTP_ET_3));
        OTP_ET_4.addTextChangedListener(new GenericTextWatcher(OTP_ET_4));


        // Resend OTP
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ThirdActivity.this, SecondActivity.class);
                intent1.putExtra("centre_name",centreName);

                startActivity(intent1);
                finish();

            }
        });
        tv_changeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ThirdActivity.this, SecondActivity.class);
                intent1.putExtra("centre_name",centreName);

                startActivity(intent1);
                finish();
            }
        });

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent1 = new Intent(ThirdActivity.this,MainActivity.class);
                startActivity(intent1);*/
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                otp = OTP_ET_1.getText().toString() +
                        OTP_ET_2.getText().toString() + OTP_ET_3.getText().toString() +
                        OTP_ET_4.getText().toString();
                if (otp.equals("null")||otp.isEmpty()||otp.length()<4){
                    Toast.makeText(ThirdActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, String> params = new HashMap<>();
                params.put("action", "verifyOtp");
                params.put("center_id", centreId);
                params.put("otp", otp);
                params.put("mobile", number);
                progressDialog.show();
                apiCall.sendData(Request.Method.POST, OTP_VERIFY, params, "sendOtp");

            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, MainActivity.class);

                startActivity(intent);
                finish();


            }
        });

    }

    private void getId() {
        imgArrowBack = findViewById(R.id.imgArrowBack);
        tv_Add = findViewById(R.id.tv_Add);
        tv_otpNum = findViewById(R.id.tv_otpNum);
        tv_resend = findViewById(R.id.tv_resend);
        tv_changeNum = findViewById(R.id.tv_changeNum);
        ll_otpBox = findViewById(R.id.ll_otpBox);
        OTP_ET_1 = findViewById(R.id.OTP_ET_1);
        OTP_ET_2 = findViewById(R.id.OTP_ET_2);
        OTP_ET_3 = findViewById(R.id.OTP_ET_3);
        OTP_ET_4 = findViewById(R.id.OTP_ET_4);
        btnValidate = findViewById(R.id.btnValidate);
        RelativeOk=findViewById(R.id.RelativeOk);
        RelativeOtp= findViewById(R.id.RelativeOtp);
        btnok=findViewById(R.id.btnok);
    }

    public void onResponse(String response, String responseType) {
        progressDialog.dismiss();
        Log.d(TAG, "onResponse: verify status ====" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            int responseCode = jsonObject.getInt("status");
            if (responseCode == 1) {
                RelativeOtp.setVisibility(View.GONE);
                RelativeOk.setVisibility(View.VISIBLE);
            }else {
                Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error, String responseType) {
        progressDialog.dismiss();
        Log.d(TAG, "onError: ====" + error);
    }


    private class GenericTextWatcher implements TextWatcher {
        private View view;

        public GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            switch (view.getId()) {
                case R.id.OTP_ET_1:
                    if (text.length() == 1)
                        OTP_ET_2.requestFocus();
                    break;
                case R.id.OTP_ET_2:
                    if (text.length() == 1)
                        OTP_ET_3.requestFocus();
                    if (text.length() == 0)
                        OTP_ET_1.requestFocus();
                    break;
                case R.id.OTP_ET_3:
                    if (text.length() == 1)
                        OTP_ET_4.requestFocus();
                    if (text.length() == 0)
                        OTP_ET_2.requestFocus();
                    break;
                case R.id.OTP_ET_4:
                    if (text.length() == 1) {
                        Log.i("verifyOTP#1", "afterTextChanged");
                        // verifyOTP();
                    }
                    if (text.length() == 0)
                        OTP_ET_3.requestFocus();
                    break;

            }
        }
    }
}
