package com.example.excelschool.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.excelschool.R;
import com.example.excelschool.interfaces.AppResponse;
import com.example.excelschool.network.ApiCall;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener , AppResponse {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView imgLogo;
    private Spinner SpinnerName;
    String selectedCentre;
    private Button BtnEnter;
    String[] centre = { "Select Your Centre","Sector 122 Noida, U.P",
            "Sector 116 Noida, U.P",
            "Sector 104 Noida, U.P",
            "Dilshad Garden, Delhi"};

    private ProgressDialog progressDialog;
    private ApiCall apiCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiCall=new ApiCall(this,this);
        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Loading...");

        getID();


        SpinnerName.setOnItemSelectedListener(this);
        BtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCentre.equals("Select Your Centre")){
                    Toast.makeText(MainActivity.this, "Please select centre", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent(MainActivity.this,SecondActivity.class);
                i.putExtra("centre_name",selectedCentre);
                startActivity(i);

            }
        });

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,centre);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerName.setAdapter(arrayAdapter);
        SpinnerName.setGravity(Gravity.CENTER);

    }




    private void getID() {
        imgLogo=findViewById(R.id.imgLogo);
        SpinnerName=findViewById(R.id.SpinnerName);
        BtnEnter=findViewById(R.id.BtnEnter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "onItemSelected: "+centre[position]);
            Log.d(TAG, "onItemSelected: "+centre[(int) id]);
            selectedCentre = centre[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResponse(String response, String responseType) {

    }

    @Override
    public void onError(String error, String responseType) {

    }
}
