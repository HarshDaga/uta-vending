package com.uta.vending;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import java.util.*;
import java.util.stream.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class ViewOperator extends AppCompatActivity
{
	ListView listOperators;
	AppDatabase appDb;

	List<User> operators;
	User selectedOperator;

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_operator);
		appDb = AppDatabase.getInstance(this);

		listOperators = findViewById(R.id.listOperators);
		listOperators.setOnItemClickListener(this::onOperatorSelected);

		appDb.userDao()
			.getAll(Role.OPERATOR)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchOperators);
	}

	private void onOperatorSelected(AdapterView<?> adapterView, View view, int position, long id)
	{
		selectedOperator = operators.get(position);

		Intent intent = new Intent(ViewOperator.this, ViewOperatorDetails.class);
		intent.putExtra("OP_ID", selectedOperator.id);
		startActivity(intent);
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	private void onFetchOperators(List<User> users)
	{
		operators = users;

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			this,
			android.R.layout.simple_list_item_1,
			operators.stream()
				.map(o -> o.id + " Name: " + o.firstName + " " + o.lastName)
				.collect(Collectors.toList())
		);
		listOperators.setAdapter(arrayAdapter);
		listOperators.invalidate();
	}
}
