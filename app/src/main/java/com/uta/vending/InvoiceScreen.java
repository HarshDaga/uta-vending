package com.uta.vending;

import android.annotation.*;
import android.os.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import java.time.format.*;
import java.util.*;
import java.util.stream.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class InvoiceScreen extends AppCompatActivity
{

	ListView lstOrders;
	Order order;
	long orderId;
	AppDatabase appDb;
	TextView cost;

	TextView orderID, date, location;

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
		orderId = getOrderIdFromIntent();
		setContentView(R.layout.activity_invoice_screen);
		cost = findViewById(R.id.textViewCostInvoice);
		lstOrders = findViewById(R.id.listOrderItemsInvoice);
		appDb = AppDatabase.getInstance(this);

		orderID = findViewById(R.id.textViewInvoiceOderId);
		date = findViewById(R.id.textViewInvoiceDate);
		location = findViewById(R.id.textViewInvoiceLocation);


		appDb.orderDao()
			.findOrder(orderId)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchOrder);
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	@SuppressLint({"DefaultLocale", "SetTextI18n"})
	private void onFetchOrder(Order order)
	{
		this.order = order;
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			InvoiceScreen.this,
			android.R.layout.simple_list_item_1,
			Arrays.stream(order.items)
				.map(item -> ("Item: " + item.item.type + ", Cost: $" + String.format("%.2f", item.getTotalCost().doubleValue()) + ", Quantity: " + item.quantity))
				.collect(Collectors.toList())
		);
		lstOrders.setAdapter(arrayAdapter);
		lstOrders.invalidate();
		cost.setText(String.format("Total Cost: $ %.2f", order.cost.doubleValue()));

		orderID.setText(this.order.id + "");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
		{
			date.setText(this.order.date.format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
		}

		location.setText(this.order.location);
	}
}
