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


public class OrdersList extends AppCompatActivity
{

	ListView lstOrders;
	List<Order> listOrders;
	long userId;
	AppDatabase appDb;

	private long getOrderIdFromIntent()
	{
		if (this.getIntent() != null)
		{
			return this.getIntent().getLongExtra("UserID", 0);
		}
		return 0;
	}

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders_list);
		appDb = AppDatabase.getInstance(this);
		userId = getOrderIdFromIntent();

		appDb.orderDao()
			.getAll(userId)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchOrders);

		lstOrders = findViewById(R.id.listOrder);
		lstOrders.setOnItemClickListener(this::onClickOrder);

	}

	private void onClickOrder(AdapterView<?> parent, View view, int position, long id)
	{
		Intent intent = new Intent(OrdersList.this, InvoiceScreen.class);
		intent.putExtra("OrderID", OrdersList.this.listOrders.get(position).id);
		startActivity(intent);
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	private void onFetchOrders(List<Order> orders)
	{
		OrdersList.this.listOrders = orders;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			@SuppressLint("DefaultLocale") ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
				OrdersList.this,
				android.R.layout.simple_list_item_1,
				orders
					.stream()
					.map(o -> "Order ID: " + o.id +

						"    Cost: $" + String.format("%.2f", o.cost.doubleValue()) +
						"    (" + o.location + ")").collect(Collectors.toList())
			);
			lstOrders.setAdapter(arrayAdapter);
			lstOrders.invalidate();
		}
    }
}
