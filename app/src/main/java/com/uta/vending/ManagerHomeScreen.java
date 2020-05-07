package com.uta.vending;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

public class ManagerHomeScreen extends AppCompatActivity
{
	Button btnViewProfile;
	Button btnLogout;
	Button btnViewOperator;
	Button btnViewVehicle;
	Button btnViewRevenue;
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
		setContentView(R.layout.activity_manager_homescreen);

		id = getIdFromIntent();

		btnViewVehicle = findViewById(R.id.ViewVehicle);
		btnViewVehicle.setOnClickListener(this::onClickViewVehicle);

		btnViewOperator = findViewById(R.id.ViewOperator);
		btnViewOperator.setOnClickListener(this::onClickViewOperator);

		btnViewRevenue = findViewById(R.id.ViewRevenue);
		btnViewRevenue.setOnClickListener(this::onClickViewRevenue);

		btnViewProfile = findViewById(R.id.ViewProfile);
		btnViewProfile.setOnClickListener(this::onClickViewProfile);

		btnLogout = findViewById(R.id.LogoutButton);
		btnLogout.setOnClickListener(this::onClickLogout);
	}

	private void onClickViewVehicle(View v)
	{
		Intent intent = new Intent(ManagerHomeScreen.this, ViewVehicle.class);
		startActivity(intent);
	}

	private void onClickViewOperator(View v)
	{
		Intent intent = new Intent(ManagerHomeScreen.this, ViewOperator.class);
		startActivity(intent);
	}

	private void onClickViewRevenue(View v)
	{
		Intent intent = new Intent(ManagerHomeScreen.this, ViewRevenue.class);
		startActivity(intent);
	}

	private void onClickViewProfile(View v)
	{
		Intent intent = new Intent(ManagerHomeScreen.this, ViewProfile.class);
		intent.putExtra("ID", id);
		startActivity(intent);
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	private void onClickLogout(View v)
	{
		Toast.makeText(ManagerHomeScreen.this, "LOGOUT Successful!", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(ManagerHomeScreen.this, MainActivity.class);
		startActivity(intent);
		finishAffinity();
	}
}
