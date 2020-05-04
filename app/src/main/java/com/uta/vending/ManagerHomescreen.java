package com.uta.vending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ManagerHomescreen extends AppCompatActivity {

    Button btnviewprofile;
    Button btnLogout;
    Button btnviewoperator;
    Button btnviewvehicle;
    Button btnviewrevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_homescreen);

        btnviewvehicle=findViewById(R.id.ViewVehicle);
        btnviewvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vvscreen = new Intent(ManagerHomescreen.this, ViewVehicle.class);
                startActivity(vvscreen);
            }
        });

        btnviewoperator=findViewById(R.id.ViewOperator);
        btnviewoperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voscreen = new Intent(ManagerHomescreen.this, ViewOperator.class);
                startActivity(voscreen);
            }
        });

        btnviewrevenue=findViewById(R.id.viewrevenue);
        btnviewrevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vrscreen=new Intent(ManagerHomescreen.this,ViewRevenue.class);
                startActivity(vrscreen);
            }
        });


        btnviewprofile=findViewById(R.id.ViewProfile);
        btnviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mgscreen = new Intent(ManagerHomescreen.this, ViewProfile.class);
                startActivity(mgscreen);
            }
        });

        btnLogout = findViewById(R.id.LogoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManagerHomescreen.this, "LOGOUT Successful!", Toast.LENGTH_SHORT).show();
                Intent backToLogin = new Intent(ManagerHomescreen.this, MainActivity.class);
                startActivity(backToLogin);
            }
        });


    }
}
