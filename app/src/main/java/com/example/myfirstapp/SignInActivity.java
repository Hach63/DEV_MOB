package com.example.myfirstapp;

import static com.example.myfirstapp.SignUpActivity.isValidEmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private TextView goToForgetPassword;
    private TextView goToSignUpACT;
    private EditText email, password;
    private Button btnSignIn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        goToForgetPassword = findViewById(R.id.goToForgetPass);
        goToSignUpACT = findViewById(R.id.goToSignUp);
        email=findViewById(R.id.emailSignIn);
        password=findViewById(R.id.passwordSignIn);
        btnSignIn=findViewById(R.id.btnSignIn);
        remember=findViewById(R.id.remember);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);





        goToForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgetPassword.class));

        });


        goToSignUpACT.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));

        });
        btnSignIn.setOnClickListener(view -> {
                progressDialog.setMessage("Please wait ...!");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkEmailVerification();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Please verify that your data is correct!", Toast.LENGTH_LONG).show();
                        }

                    }
                });

        });
        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        boolean isChecked= preferences.getBoolean("remember",false);
        if (isChecked){
            startActivity(new Intent(SignInActivity.this, ProfilActivity.class));
        }
        else {
            Toast.makeText(this, "Please login!", Toast.LENGTH_SHORT).show();
        }
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("remember",true);
                    editor.apply();
                }
                else{
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("remember",false);
                    editor.apply();

                }
            }
        });
    }



    private void checkEmailVerification() {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user.isEmailVerified()){

            startActivity(new Intent(SignInActivity.this,ProfilActivity.class));
            progressDialog.dismiss();

        }
        else{
            Toast.makeText(this, "Please verify your email!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            progressDialog.dismiss();
        }
    }
}