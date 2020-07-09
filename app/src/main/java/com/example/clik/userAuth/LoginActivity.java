package com.example.clik.userAuth;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        TextView info = findViewById(R.id.info);
        String text = "We will send you an One Time Password (OTP) on this mobile number.";
        SpannableString ss = new SpannableString(text);
        StyleSpan boldspan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldspan,20,43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        info.setText(ss);

        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = number_edit.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    number_edit.setError("Valid Number Is Required");
                    number_edit.requestFocus();
                }
                else{
                    final String phonenumber = "+" + code + number;

                    Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                    intent.putExtra("phonenumber", phonenumber);
                    intent.putExtra("status", "register");
                    startActivity(intent);
                }
            }
        });

    }
}