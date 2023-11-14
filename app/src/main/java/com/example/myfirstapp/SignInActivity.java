package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    private TextView goToForgetPassword;
    private  TextView goToSignUpACT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        goToForgetPassword = findViewById(R.id.goToForgetPass);

        goToForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgetPassword.class));

        });
        goToSignUpACT = findViewById(R.id.goToSignUp);

        goToSignUpACT.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));

        });
    }
}