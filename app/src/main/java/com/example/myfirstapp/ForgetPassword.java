package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class ForgetPassword extends AppCompatActivity {

    private Button goToSignInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        goToSignInActivity = findViewById(R.id.goToSignIn);

        goToSignInActivity.setOnClickListener(v -> {
            startActivity(new Intent(ForgetPassword.this, SignInActivity.class));

        });
    }
}