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

public class EditCart extends AppCompatActivity
{
	Button btnCheckout;
	ListView lstOrders;

	Order order;
	AppDatabase appDb;
	TextView cost;
	long orderId;

	@SuppressLint("DefaultLocale")
	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_cart);
		order = ViewInventory.cartOrder;
		lstOrders = findViewById(R.id.listOrderItems1);
		cost = findViewById(R.id.textViewCost1);
		appDb = AppDatabase.getInstance(this);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			EditCart.this,
			android.R.layout.simple_list_item_1,
			Arrays.stream(order.items)
				.map(item -> ("Item: " + item.item.type + ", Cost: $" + String.format("%.2f", item.item.cost.doubleValue()) + ", Quantity: " + item.quantity))
				.collect(Collectors.toList())
		);
		lstOrders.setAdapter(arrayAdapter);
		lstOrders.invalidate();
		cost.append(" " + String.format("%.2f", order.cost.doubleValue()));

		btnCheckout = findViewById(R.id.btnCheckout1);
		btnCheckout.setOnClickListener(this::onClickCheckout);
	}

	@SuppressLint("CheckResult")
	private void onClickCheckout(View v)
	{
		appDb.orderDao()
			.insert(order)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onInsertOrder);
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	@SuppressLint("CheckResult")
	private void onInsertOrder(long orderId)
	{
		this.orderId = orderId;
		appDb.vehicleDao().getInventory(order.vehicleId)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(vehicleInventory ->
			{
				ViewInventory.cartOrder = null;
				for (OrderItem orderItem : order.items)
				{
					vehicleInventory.inventory
						.stream()
						.filter(inventoryItem -> inventoryItem.item.type.equals(orderItem.item.type))
						.collect(Collectors.toList())
						.get(0)
						.quantity -= orderItem.quantity;
				}

				InventoryItem[] items = vehicleInventory
					.inventory
					.toArray(new InventoryItem[0]);

				appDb.inventoryDao()
					.update(items)
					.subscribeOn(Schedulers.computation())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(this::onUpdateInventory);
			});
	}

	private void onUpdateInventory()
	{
		Intent intent = new Intent(EditCart.this, InvoiceScreen.class);
		intent.putExtra("OrderID", this.orderId);
		startActivity(intent);
	}
}
