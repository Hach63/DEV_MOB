package com.example.myfirstapp;

import static com.google.android.gms.common.util.CollectionUtils.isEmpty;
import static com.google.firebase.database.snapshot.EmptyNode.Empty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView iconMenu;
    private Button boutonAdd;
    private EditText name,value;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private ListView listDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout=findViewById(R.id.drawer_layout_home);
        navigationView=findViewById(R.id.navigation_view_home);
        iconMenu=findViewById(R.id.icon_home);
        boutonAdd=findViewById(R.id.btnAddD);
        name=findViewById(R.id.deviceName);
        value=findViewById(R.id.deviceValue);
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        listDevices=findViewById(R.id.listDevices);

        navigationDrawer();



        boutonAdd.setOnClickListener(v -> {
            if (name.getText().toString().isEmpty()) {
                name.setError("Invalid device's name !");
            }else  if (value.getText().toString().isEmpty()) {
                value.setError("Invalid value !");
            }else{


            String nameS=name.getText().toString();
            String valueS=value.getText().toString();
            HashMap<String , String> deviceMap= new HashMap<>();
            deviceMap.put("name",nameS);
            deviceMap.put("value",valueS);
            reference.child("Devices").push().setValue(deviceMap);
            name.setText("");
            value.setText("");
            name.clearFocus();
            value.clearFocus();
            Toast.makeText(this, "New device added successfully", Toast.LENGTH_SHORT).show();
            }


        });

        ArrayList<String> deviceArrayList=new ArrayList<>();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(HomeActivity.this,R.layout.list_item,deviceArrayList);
        listDevices.setAdapter(adapter);
        DatabaseReference deviceReference=firebaseDatabase.getReference().child("Devices");
        deviceReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deviceArrayList.clear();
                for(DataSnapshot deviceSnapchot:snapshot.getChildren()){
                    deviceArrayList.add(deviceSnapchot.child("name").getValue()+" : "+deviceSnapchot.child("value").getValue());
                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, ""+error, Toast.LENGTH_SHORT).show();

            }
        });


        navigationView.setNavigationItemSelectedListener( item -> {
            if(item.getItemId()==R.id.devices){
                drawerLayout.closeDrawer(GravityCompat.START);

            }
            if (item.getItemId()==R.id.tiquetsElectriques) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(HomeActivity.this,TiquetElectricActivity.class));
            }
            if (item.getItemId()==R.id.profile) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(HomeActivity.this,ProfilActivity.class));
            }
            return true;
        });

    }



    private void navigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.devices);
        navigationView.bringToFront();

        iconMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
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

    }
}
