package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;

    private EditText fullname, email, phone, cin;
    private Button btnEdit, btnLogOut;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser loggedUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        btnEdit = findViewById(R.id.editP);
        btnLogOut = findViewById(R.id.logoutP);
        fullname = findViewById(R.id.idNameP);
        email = findViewById(R.id.idEmailP);
        cin = findViewById(R.id.idCinP);
        phone = findViewById(R.id.idPhoneP);
        drawerLayout = findViewById(R.id.drawer_layout_profil);
        navigationView = findViewById(R.id.navigation_view_profil);
        iconMenu = findViewById(R.id.icon_profil);

        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.devices) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(ProfilActivity.this,HomeActivity.class));

            }

            if (item.getItemId() == R.id.tiquetsElectriques) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(ProfilActivity.this,TiquetElectricActivity.class));
            }
            if (item.getItemId() == R.id.profile) {
                drawerLayout.closeDrawer(GravityCompat.START);

            }


            return true;
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        loggedUser = firebaseAuth.getCurrentUser();
        reference = firebaseDatabase.getReference().child("Users").child(loggedUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullNameS = snapshot.child("fullName").getValue().toString();
                String emailS = snapshot.child("email").getValue().toString();
                String cinS = snapshot.child("cin").getValue().toString();
                String phoneS = snapshot.child("phone").getValue().toString();
                fullname.setText(fullNameS);
                email.setText(emailS);
                cin.setText(cinS);
                phone.setText(phoneS);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilActivity.this, "" + error, Toast.LENGTH_LONG).show();
            }
        });
        btnLogOut.setOnClickListener(view -> {
            startActivity(new Intent(ProfilActivity.this, SignInActivity.class));
            SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("remember", false);
            editor.apply();
        });
        btnEdit.setOnClickListener(view -> {
            fullname.setFocusableInTouchMode(true);
            phone.setFocusableInTouchMode(true);
            cin.setFocusableInTouchMode(true);
            btnEdit.setText("SAVE");
            btnEdit.setOnClickListener(view1 -> {
                String newFullName = fullname.getText().toString();
                String newCin = cin.getText().toString();
                String newPhone = phone.getText().toString();
                reference.child("fullName").setValue(newFullName);
                reference.child("cin").setValue(newCin);
                reference.child("phone").setValue(newPhone);
                Toast.makeText(this, "Your Data has been changed successfully !", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProfilActivity.this, ProfilActivity.class));
            });
        });


    }

    private void navigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.profile);
        navigationView.bringToFront();

        iconMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorAPP));
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;

    }


    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();

    }


}