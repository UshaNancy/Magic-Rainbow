package com.grootan.magic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText emailId;
    private TextInputEditText password;
    private Button signUp;
    private TextView loginFromSignup;
    private String strEmail;
    private String strPassword;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = emailId.getText().toString().trim();
                strPassword = password.getText().toString().trim();
                validate();
            }
        });

        loginFromSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void validate() {
        if (strEmail.isEmpty()) {
            emailId.setError("cannot be empty");
        } else if (strPassword.isEmpty()) {
            password.setError("cannot be empty");
        } else {
            register();
        }
    }

    private void register() {
        firebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                Log.e("login", "--success");
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.putExtra("emailId", strEmail);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());

                } catch (Exception e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        emailId = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signUp = findViewById(R.id.signUp_button);
        loginFromSignup = findViewById(R.id.login_from_signup);
    }
}
