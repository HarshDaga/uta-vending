package com.uta.vending;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

public class OperatorHomeScreen extends AppCompatActivity
{
	Button btnLogout;
	Button btnViewProfile;
	Button btnViewSchedule;
	Button btnProcessOrder;
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
		setContentView(R.layout.activity_operator_homescreen);

		id = getIdFromIntent();

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

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	private void onClickLogout(View v)
	{
		Toast.makeText(OperatorHomeScreen.this, "LOGOUT Successful!", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(OperatorHomeScreen.this, MainActivity.class);
		startActivity(intent);
		finishAffinity();
	}

	private void onClickViewProfile(View v)
	{
		Intent intent = new Intent(OperatorHomeScreen.this, ViewProfile.class);
		intent.putExtra("ID", id);
		startActivity(intent);
	}
}
