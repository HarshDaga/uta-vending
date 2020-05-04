package com.uta.vending;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

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
		setContentView(R.layout.activity_after_login);

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
		startActivity(intent);
	}

	private void onClickViewProfile(View v)
	{
		Intent intent = new Intent(UserHomeScreen.this, ViewProfile.class);
		intent.putExtra("ID", id);
		startActivity(intent);
	}

	private void onClickLogout(View v)
	{
		Toast.makeText(UserHomeScreen.this, "LOGOUT Successful!", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(UserHomeScreen.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void onClickViewOrder(View v)
	{
		Intent intent = new Intent(UserHomeScreen.this, InvoiceScreen.class);
		startActivity(intent);
	}

	private void onClickViewCart(View v)
	{
		Intent intent = new Intent(UserHomeScreen.this, EditCart.class);
		startActivity(intent);
	}
}
