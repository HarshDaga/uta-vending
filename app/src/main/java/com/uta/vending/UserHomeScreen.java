package com.uta.vending;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

public class UserHomeScreen extends AppCompatActivity
{
    Button btnLogout;
    Button btnViewProfile;
    Button btnSearchVehicle;
    Button btnViewOrder;
    Button btnViewCart;
    long id;

    private long getIdFromIntent()
    {
        if (this.getIntent() != null)
        {
            return this.getIntent().getLongExtra("ID", 0);
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_homescreen);

        id = getIdFromIntent();

        btnSearchVehicle = findViewById(R.id.SearchVehicle);
        btnSearchVehicle.setOnClickListener(this::onClickSearchVehicle);

        btnViewProfile = findViewById(R.id.ViewProfile);
        btnViewProfile.setOnClickListener(this::onClickViewProfile);

        btnLogout = findViewById(R.id.LogoutButton);
        btnLogout.setOnClickListener(this::onClickLogout);

        btnViewOrder = findViewById(R.id.ViewOrder);
        btnViewOrder.setOnClickListener(this::onClickViewOrder);

        btnViewCart = findViewById(R.id.ViewCart);
        btnViewCart.setOnClickListener(this::onClickViewCart);
    }

    private void onClickSearchVehicle(View v)
    {
        Intent intent = new Intent(UserHomeScreen.this, SearchVehicle.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    private void onClickViewProfile(View v)
    {
        Intent intent = new Intent(UserHomeScreen.this, ViewProfile.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void onClickLogout(View v)
    {
        Toast.makeText(UserHomeScreen.this, "LOGOUT Successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UserHomeScreen.this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void onClickViewOrder(View v)
    {
        Intent intent = new Intent(UserHomeScreen.this, OrdersList.class);
        intent.putExtra("UserID", id);
        startActivity(intent);
    }

    private void onClickViewCart(View v)
    {
        if (ViewInventory.cartOrder == null)
        {
            Toast.makeText(this, "No order started. Go to Search Vehicle.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(UserHomeScreen.this, ViewInventory.class);
        startActivity(intent);
    }
}
