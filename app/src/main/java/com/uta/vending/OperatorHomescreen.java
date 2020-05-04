package com.uta.vending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OperatorHomescreen extends AppCompatActivity {

    Button btnviewprofile;
    Button btnviewschedule;
    Button btnprocessorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_homescreen);

        btnprocessorder=findViewById(R.id.ProcessOrder);
        btnprocessorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oporder=new Intent(OperatorHomescreen.this, ProcessOrder.class);
                startActivity(oporder);
            }
        });

        btnviewschedule=findViewById(R.id.ViewSchedule);
        btnviewschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opschedule=new Intent(OperatorHomescreen.this, ViewSchedule.class);
                startActivity(opschedule);
            }
        });


        btnviewprofile=findViewById(R.id.ViewProfile);
        btnviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ophomescreen = new Intent(OperatorHomescreen.this, ViewProfile.class);
                startActivity(ophomescreen);
            }
        });

    }
}
