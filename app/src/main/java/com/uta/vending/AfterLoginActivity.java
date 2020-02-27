package com.uta.vending;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AfterLoginActivity extends AppCompatActivity
{

    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        btnLogout = findViewById(R.id.LogoutButton);
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(AfterLoginActivity.this, "LOGOUT Successful!", Toast.LENGTH_SHORT).show();
                Intent backToLogin = new Intent(AfterLoginActivity.this, MainActivity.class);
                startActivity(backToLogin);
            }
        });

    }
}
