package com.example.clik.userAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clik.R;

public class LoginActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText number_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        number_edit = findViewById(R.id.mobile_number);
        Button get_otp = findViewById(R.id.get_otp);

        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = number_edit.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    number_edit.setError("Valid Number Is Required");
                    number_edit.requestFocus();
                }

                final String phonenumber = "+" + code + number;

                Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phonenumber", phonenumber);
                intent.putExtra("status", "register");
                startActivity(intent);
            }
        });

    }
}