package com.uta.vending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewOperator extends AppCompatActivity {

    Button btnviewopdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_operator);

        btnviewopdetails=findViewById(R.id.ViewOperatorDetails);
        btnviewopdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opdscreen=new Intent(ViewOperator.this, ViewOperatorDetails.class);
                startActivity(opdscreen);
            }
        });


    }
}
