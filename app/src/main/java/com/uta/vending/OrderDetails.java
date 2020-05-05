package com.uta.vending;

import android.annotation.*;
import android.os.*;
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
	Order order;
	long orderId;
	AppDatabase appDb;
	TextView cost;

	private long getOrderIdFromIntent()
	{
		if (this.getIntent() != null)
		{
			return this.getIntent().getLongExtra("OrderID", 0);
		}
		return 0;
	}


	@SuppressLint("CheckResult")
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		appDb = AppDatabase.getInstance(this);
		orderId = getOrderIdFromIntent();
		cost = findViewById(R.id.textViewCost);
		lstOrders = findViewById(R.id.listOrderItems);
//
//        lstOrders.

		appDb.orderDao()
			.findOrder(orderId)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchOrder);
	}

	@SuppressLint("DefaultLocale")
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void onFetchOrder(Order order)
	{
		this.order = order;
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			OrderDetails.this,
			android.R.layout.simple_list_item_1,
			Arrays.stream(order.items)
				.map(item -> ("Item: " + item.item.type + ", Cost: $" + String.format("%.2f", item.item.cost.doubleValue()) + ", Quantity: " + item.quantity))
				.collect(Collectors.toList())
		);
		lstOrders.setAdapter(arrayAdapter);
		lstOrders.invalidate();
		cost.append(" " + String.format("%.2f", order.cost.doubleValue()));
	}
}
