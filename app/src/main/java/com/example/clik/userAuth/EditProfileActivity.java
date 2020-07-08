package com.example.clik.userAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clik.MainActivity;
import com.example.clik.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private EditText display_name;
    private EditText email;
    private Button register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        display_name = findViewById(R.id.display_name);

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email_text = email.getText().toString().trim();
                final String display_name_text = display_name.getText().toString().trim();

                if(email_text.isEmpty()){
                    email.setError("Empty");
                    email.requestFocus();
                }
                if(display_name_text.isEmpty()){
                    display_name.setError("Empty");
                    display_name.requestFocus();
                }
                else{
                    final FirebaseUser user = mAuth.getCurrentUser();

                    assert user != null;
                    user.updateEmail(email_text).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("Name", display_name_text);
                            map.put("Bio", "");
                            map.put("Email", email_text);
                            map.put("PhoneNumber", user.getPhoneNumber());
                            FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                                    }
                                    else{
                                        Toast.makeText(EditProfileActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}