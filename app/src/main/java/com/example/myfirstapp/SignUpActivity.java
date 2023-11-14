package com.example.myfirstapp;

import static androidx.constraintlayout.motion.widget.TransitionBuilder.validate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    //declaration des instances
    //declaration des variables
    private TextView goToSignIn;
    private EditText etFullName, etEmail, etCin, etPhone, etPassword;
    private Button btnSignUp;

    private String fullNameS, emailS, cinS, phoneS, passwordS;

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //affectation des views xml-> java
        goToSignIn = findViewById(R.id.goToSignIn);
        goToSignIn = findViewById(R.id.goToSignIn);
        etFullName = findViewById(R.id.idName);
        etEmail = findViewById(R.id.idEmail);
        etCin = findViewById(R.id.idCin);
        etPhone = findViewById(R.id.idPhone);
        etPassword = findViewById(R.id.idPassword);
        btnSignUp = findViewById(R.id.idButtonSignUp);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        goToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });
        btnSignUp.setOnClickListener(view -> {
            if (validate()){
                //Toast.makeText(this, "VALIDE!", Toast.LENGTH_LONG).show();
                progressDialog.setMessage("Please wait ...!");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(emailS.trim(),passwordS.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendVerificationMail();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

                
            }
        });
    }

    private void sendVerificationMail() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        firebaseAuth.signOut();
                        Toast.makeText(SignUpActivity.this, "Account created successfully ! please check your Mail", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                    else Toast.makeText(SignUpActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validate() {
        boolean result=false;
        fullNameS=etFullName.getText().toString();
        emailS =etEmail.getText().toString();
        cinS=etCin.getText().toString();
        phoneS=etPhone.getText().toString();
        passwordS=etPassword.getText().toString();
        if (fullNameS.isEmpty() || fullNameS.length() < 7) {
            etFullName.setError("Invalid full name !");


        } else if (!isValidEmail(emailS)) {
            etEmail.setError("Invalid Email!");
        } else if (cinS.isEmpty() || cinS.length() != 8) {
            etCin.setError("Invalid CIN !");
        } else if (phoneS.isEmpty() || phoneS.length() != 8) {
            etPhone.setError("Invalid phone!");
        } else if (passwordS.isEmpty() || passwordS.length() < 6) {
            etPassword.setError("Invalid password!");
        } else result = true;
        return result;
    }
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}