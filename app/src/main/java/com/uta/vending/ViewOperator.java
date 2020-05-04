package com.uta.vending;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import java.util.*;

public class ViewOperator extends AppCompatActivity
{
	Button btnViewOpDetails;
	AppDatabase appDb;

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_operator);
		appDb = AppDatabase.getInstance(this);

		btnViewOpDetails = findViewById(R.id.BtnViewOperatorDetails);
		btnViewOpDetails.setOnClickListener(this::onClickDetails);

		appDb.userDao()
			.getAll(Role.OPERATOR)
			.subscribe(this::onFetchOperators);
	}

	private void onFetchOperators(List<User> users)
	{
		// TODO Populate UI
	}

	private void onClickDetails(View v)
	{
		Intent intent = new Intent(ViewOperator.this, ViewOperatorDetails.class);
		startActivity(intent);
	}
}
