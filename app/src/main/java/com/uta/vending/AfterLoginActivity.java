package com.uta.vending;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;

public class AfterLoginActivity extends AppCompatActivity
{

	Button btnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_login);

		btnLogout = findViewById(R.id.LogoutButton);
		btnLogout.setOnClickListener(this::onClickLogout);

	}

	private void onClickLogout(View v)
	{
		Toast.makeText(AfterLoginActivity.this, "LOGOUT Successful!", Toast.LENGTH_SHORT).show();
		Intent backToLogin = new Intent(AfterLoginActivity.this, MainActivity.class);
		startActivity(backToLogin);
	}
}
