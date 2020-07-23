package com.grootan.magic;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailId;
    private TextInputEditText password;
    private Button login;
    private TextView signupFromLogin;
    private String strEmail;
    private String strPassword;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!currentUser()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_login);
            init();

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    strEmail = emailId.getText().toString().trim();
                    strPassword = password.getText().toString().trim();
                    validate();
                }
            });
            signupFromLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void validate() {
        if (strEmail.isEmpty()) {
            emailId.setError("cannot be empty");
        } else if (strPassword.isEmpty()) {
            password.setError("cannot be empty");
        } else {
            doLogin();
        }
    }

    private void doLogin() {
        firebaseAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                Log.e("login", "--success");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //   intent.putExtra("emailId",strEmail);


                startActivity(intent);
                finish();
                SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("emailId", strEmail);
                editor.commit();
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());

                } catch (Exception e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(LoginActivity.this, "Log in failed " + "\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        emailId = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        signupFromLogin = findViewById(R.id.singUp_from_login);
    }

    public boolean currentUser() {
        return firebaseAuth.getCurrentUser() == null;
    }
}
