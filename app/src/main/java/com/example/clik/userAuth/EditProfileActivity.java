package com.example.clik.userAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private EditText display_name;
    private EditText email;
    private String default_pic = "https://www.kindpng.com/picc/m/24-248253_user-profile-default-image-png-clipart-png-download.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        email = findViewById(R.id.email);
        display_name = findViewById(R.id.display_name);
        final ImageView profile_pic = findViewById(R.id.profile_pic);

        Button register = findViewById(R.id.register);

        Picasso.get().load(default_pic).networkPolicy(NetworkPolicy.OFFLINE).into(profile_pic, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(default_pic).into(profile_pic);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this);
                pd.setMessage("Logging In");
                pd.show();
                final String email_text = email.getText().toString().trim();
                final String display_name_text = display_name.getText().toString().trim();

                if (email_text.isEmpty()) {
                    email.setError("Empty");
                    email.requestFocus();
                }
                if (display_name_text.isEmpty()) {
                    display_name.setError("Empty");
                    display_name.requestFocus();
                } else {
                    assert fuser != null;
                    fuser.updateEmail(email_text).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("uId", fuser.getUid());
                            map.put("Name", display_name_text);
                            map.put("Bio", "");
                            map.put("ProfileUri", default_pic);
                            FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EditProfileActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                                    } else {
                                        Toast.makeText(EditProfileActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    pd.dismiss();
                                    finish();
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}