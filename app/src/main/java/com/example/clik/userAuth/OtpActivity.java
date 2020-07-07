package com.example.clik.userAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.clik.R;

public class OtpActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText number_edit;
    private Button get_otp;
    private TextView login_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        number_edit = findViewById(R.id.mobile_number);
        get_otp = findViewById(R.id.get_otp);
        login_activity = findViewById(R.id.login_page);

        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = number_edit.getText().toString().trim();

                if(number.isEmpty() || number.length() < 10){
                    number_edit.setError("Valid Number Is Required");
                    number_edit.requestFocus();
                }

                String phonenumber = "+" + code + number;

                Intent intent = new Intent(OtpActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                startActivity(intent);
            }
        });

    }
}