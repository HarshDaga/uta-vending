package com.uta.vending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProcessOrder extends AppCompatActivity {
    Button btnprocessorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_order);

        btnprocessorder=findViewById(R.id.processorder);
        btnprocessorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vodscreen=new Intent(ProcessOrder.this,OrderDetails.class);
                startActivity(vodscreen);
            }
        });
    }
}
