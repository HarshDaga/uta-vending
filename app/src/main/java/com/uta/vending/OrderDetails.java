package com.uta.vending;

import android.annotation.*;
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

public class OrderDetails extends AppCompatActivity
{
	ListView lstOrders;
	Button btnServe;
	Order order;
	long orderId;
	AppDatabase appDb;

	private long getOrderIdFromIntent()
	{
		if (this.getIntent() != null)
		{
			return this.getIntent().getLongExtra("OrderID", 0);
		}
		return 0;
	}


	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		btnServe = findViewById(R.id.btnServe);
		appDb = AppDatabase.getInstance(this);
		orderId = getOrderIdFromIntent();
		lstOrders = findViewById(R.id.listOrderItems);

		btnServe.setOnClickListener(this::onClickServe);

		appDb.orderDao()
			.findOrder(orderId)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchOrder);
	}

	@SuppressLint("CheckResult")
	private void onClickServe(View view)
	{
		if (order.isServed)
		{
			Toast.makeText(this, "Order already processed", Toast.LENGTH_SHORT).show();
			return;
		}

		order.isServed = true;
		appDb.orderDao()
			.update(order)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onUpdateOrder);
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	@SuppressLint("DefaultLocale")
	private void onFetchOrder(Order order)
	{
		this.order = order;
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			OrderDetails.this,
			android.R.layout.simple_list_item_1,
			Arrays.stream(order.items)
				.map(item -> ("Item: " + item.item.type + ", Quantity: " + item.quantity))
				.collect(Collectors.toList())
		);
		lstOrders.setAdapter(arrayAdapter);
		lstOrders.invalidate();
	}

	private void onUpdateOrder()
	{
		Toast.makeText(this, "SUCCESS: Order processed", Toast.LENGTH_LONG).show();
	}
}
