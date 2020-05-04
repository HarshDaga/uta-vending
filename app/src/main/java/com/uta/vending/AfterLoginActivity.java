package com.uta.vending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AfterLoginActivity extends AppCompatActivity
{

    Button btnLogout;
    Button btnviewprofile;
    Button btnsearchvehicle;
    Button btnvieworder;
    Button btnviewcart;
    long id;


    public static Intent getNavigationIntent(Context ctx, long id) {
        Intent lIntent = new Intent(ctx, AfterLoginActivity.class);
        if (id != 0) {
            lIntent.putExtra("ID", id);
        }
        return lIntent;
    }

    public long getIdFromIntent(){

        if (this.getIntent() != null){
            return this.getIntent().getLongExtra("ID",0);
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        id = getIdFromIntent();

        btnsearchvehicle=findViewById(R.id.SearchVehicle);
        btnsearchvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent svscreen = new Intent(AfterLoginActivity.this, SearchVehicle.class);
                startActivity(svscreen);
            }
        });


        btnviewprofile=findViewById(R.id.ViewProfile);
        btnviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vpscreen = ViewProfile.getNavigationIntent(AfterLoginActivity.this, id);
                startActivity(vpscreen);
            }
        });

        btnLogout = findViewById(R.id.LogoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(AfterLoginActivity.this, "LOGOUT Successful!", Toast.LENGTH_SHORT).show();
                Intent backToLogin = new Intent(AfterLoginActivity.this, MainActivity.class);
                startActivity(backToLogin);
                finish();
            }
        });

        btnvieworder=findViewById(R.id.btnvieworder);
        btnvieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voscreen=new Intent(AfterLoginActivity.this, InvoiceScreen.class);
                startActivity(voscreen);
            }
        });

        btnviewcart=findViewById(R.id.btnviewcart);
        btnviewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ecscreen=new Intent(AfterLoginActivity.this, EditCart.class);
                startActivity(ecscreen);
            }
        });



    }
}
