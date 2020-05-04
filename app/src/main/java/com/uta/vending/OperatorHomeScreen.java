package com.uta.vending;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;

public class OperatorHomeScreen extends AppCompatActivity
{
	Button btnLogout;
	Button btnViewProfile;
	Button btnViewSchedule;
	Button btnProcessOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operator_homescreen);

		btnProcessOrder = findViewById(R.id.ProcessOrder);
		btnProcessOrder.setOnClickListener(this::onClickProcessOrder);

		btnViewSchedule = findViewById(R.id.ViewSchedule);
		btnViewSchedule.setOnClickListener(this::onClickViewSchedule);

		btnViewProfile = findViewById(R.id.ViewProfile);
		btnViewProfile.setOnClickListener(this::onClickViewProfile);

		btnLogout = findViewById(R.id.LogoutButton);
		btnLogout.setOnClickListener(this::onClickLogout);
	}

	private void onClickProcessOrder(View v)
	{
		Intent intent = new Intent(OperatorHomeScreen.this, ProcessOrder.class);
		startActivity(intent);
	}

	private void onClickViewSchedule(View v)
	{
		Intent intent = new Intent(OperatorHomeScreen.this, ViewSchedule.class);
		startActivity(intent);
	}

	private void onClickLogout(View v)
	{
		Toast.makeText(OperatorHomeScreen.this, "LOGOUT Successful!", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(OperatorHomeScreen.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void onClickViewProfile(View v)
	{
		Intent intent = new Intent(OperatorHomeScreen.this, ViewProfile.class);
		//TODO: intent.putExtra("ID", id);
		startActivity(intent);
	}
}
