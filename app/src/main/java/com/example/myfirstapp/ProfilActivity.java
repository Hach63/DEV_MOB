package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfilActivity extends AppCompatActivity {
private EditText fullname,email,phone,cin;
private Button btnEdit,btnLogOut;
private FirebaseAuth firebaseAuth;
private FirebaseDatabase firebaseDatabase;
private FirebaseUser loggedUser;
private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        btnEdit= findViewById(R.id.editP);
        btnLogOut= findViewById(R.id.logoutP);
        fullname= findViewById(R.id.idNameP);
        email= findViewById(R.id.idEmailP);
        cin= findViewById(R.id.idCinP);
        phone= findViewById(R.id.idPhoneP);

       firebaseAuth=FirebaseAuth.getInstance();
       firebaseDatabase=FirebaseDatabase.getInstance();
       loggedUser=firebaseAuth.getCurrentUser();
       reference=firebaseDatabase.getReference().child("Users").child(loggedUser.getUid());

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String fullNameS=snapshot.child("fullName").getValue().toString();
               String emailS=snapshot.child("email").getValue().toString();
               String cinS=snapshot.child("cin").getValue().toString();
               String phoneS=snapshot.child("phone").getValue().toString();
               fullname.setText(fullNameS);
               email.setText(emailS);
               cin.setText(cinS);
               phone.setText(phoneS);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(ProfilActivity.this, ""+error, Toast.LENGTH_LONG).show();
           }
       });
       btnLogOut.setOnClickListener(view ->{
           startActivity(new Intent(ProfilActivity.this,SignInActivity.class));
           SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
           SharedPreferences.Editor editor=preferences.edit();
           editor.putBoolean("remember",false);
           editor.apply();
       });
       btnEdit.setOnClickListener(view -> {
           fullname.setFocusableInTouchMode(true);
           phone.setFocusableInTouchMode(true);
           cin.setFocusableInTouchMode(true);
           btnEdit.setText("SAVE");
           btnEdit.setOnClickListener(view1 -> {
               String newFullName=fullname.getText().toString();
               String newCin=cin.getText().toString();
               String newPhone=phone.getText().toString();
               reference.child("fullName").setValue(newFullName);
               reference.child("cin").setValue(newCin);
               reference.child("phone").setValue(newPhone);
               Toast.makeText(this, "Your Data has been changed successfully !", Toast.LENGTH_LONG).show();
               startActivity(new Intent(ProfilActivity.this,ProfilActivity.class));
           });
       });


    }


}