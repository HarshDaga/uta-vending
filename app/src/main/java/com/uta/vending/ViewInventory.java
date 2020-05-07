package com.uta.vending;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class ViewInventory extends AppCompatActivity
{
	public static Order cartOrder;
	public static long vehicleId, userId;
	public static Vehicle currentVehicle;
	public static List<InventoryItem> inventoryItemList;
	AppDatabase appDb;
	ListView listInventory;
	Button addToCart;
	EditText editTextDrinks, editTextSnacks, editTextSandwiches;

	private void getIdFromIntent()
	{
		if (this.getIntent() != null)
		{
			vehicleId = this.getIntent().getLongExtra("VehicleID", 0);
			userId = this.getIntent().getLongExtra("UserID", 0);
		}
	}

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_inventory);
		appDb = AppDatabase.getInstance(this);

		editTextSnacks = findViewById(R.id.editTextSnacks);
		editTextDrinks = findViewById(R.id.editTextDrinks);
		editTextSandwiches = findViewById(R.id.editTextSandwiches);
		addToCart = findViewById(R.id.btnAddToCart);
		listInventory = findViewById(R.id.listInventory);

		getIdFromIntent();
		if (ViewInventory.cartOrder != null)
			updateUiQuantities();

		addToCart.setOnClickListener(this::onClickAddToCart);

		if (vehicleId > 0)
		{
			appDb.vehicleDao()
				.find(vehicleId)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(this::onVehicleGet, this::onLookupError);
		}
	}

	@SuppressLint("SetTextI18n")
	private void updateUiQuantities()
	{
		for (OrderItem item : cartOrder.items)
		{
			switch (item.item.type)
			{
				case "Sandwich":
					editTextSandwiches.setText(Integer.toString(item.quantity));
					break;
				case "Snacks":
					editTextSnacks.setText(Integer.toString(item.quantity));
					break;
				case "Drink":
					editTextDrinks.setText(Integer.toString(item.quantity));
					break;
			}
		}
	}

	private void onLookupError(Throwable throwable)
	{
		Log.e("ViewInventory", "Error", throwable);
		Toast.makeText(ViewInventory.this, "Error", Toast.LENGTH_LONG).show();
	}

	@SuppressLint("CheckResult")
	private void onVehicleGet(Vehicle vehicle)
	{
		currentVehicle = vehicle;
		appDb.inventoryDao()
			.getInventory(currentVehicle.id)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onInventoryGet, this::onLookupError);
	}

	@SuppressLint("DefaultLocale")
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void onInventoryGet(VehicleInventory vehicleInventory)
	{
		inventoryItemList = vehicleInventory.inventory;
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			this,
			android.R.layout.simple_list_item_1,
			vehicleInventory.inventory.stream()
				.map(item -> "Item: " + item.item.type + ", Cost: $" + String.format("%.2f", item.item.cost.doubleValue()) + ", Quantity: " + item.quantity)
				.collect(Collectors.toList())
		);
		listInventory.setAdapter(arrayAdapter);
		listInventory.invalidate();
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private void onClickAddToCart(View v)
	{
		int sandwiches = 0, snacks = 0, drinks = 0;

		try
		{
			if (!"".equals((editTextSandwiches.getText().toString().trim())))
				sandwiches = Integer.parseInt(editTextSandwiches.getText().toString().trim());
			if (!"".equals((editTextSnacks.getText().toString().trim())))
				snacks = Integer.parseInt(editTextSnacks.getText().toString().trim());
			if (!"".equals((editTextDrinks.getText().toString().trim())))
				drinks = Integer.parseInt(editTextDrinks.getText().toString().trim());
		} catch (Exception ex)
		{
			Log.e("Error", "Error", ex);
			Toast.makeText(ViewInventory.this, "Enter correct quantities and try again.", Toast.LENGTH_SHORT).show();
		}

		if ((sandwiches | snacks | drinks) == 0)
		{
			Toast.makeText(this, "No items selected", Toast.LENGTH_SHORT).show();
			return;
		}

		Order order = new Order(
			userId,
			currentVehicle.scheduleToday.operatorId,
			LocalDateTime.now(),
			vehicleId,
			currentVehicle.scheduleToday.location);

		boolean failed = false;

		for (InventoryItem item : inventoryItemList)
		{
			if (!failed && item.item.type.equals("Sandwich") && sandwiches > 0)
			{
				if (item.quantity - sandwiches >= 0)
					order.addItem(new OrderItem(item.item, sandwiches));
				else
					failed = true;
			}
			if (!failed && item.item.type.equals("Snacks") && snacks > 0)
			{
				if (item.quantity - snacks >= 0)
					order.addItem(new OrderItem(item.item, snacks));
				else
					failed = true;
			}
			if (!failed && item.item.type.equals("Drink") && drinks > 0)
			{
				if (item.quantity - drinks >= 0)
				{
					order.addItem(new OrderItem(item.item, drinks));
				}
				else
					failed = true;
			}
		}

		if (!failed)
		{
			order.isServed = false;
			cartOrder = order;
			Intent intent = new Intent(ViewInventory.this, EditCart.class);
			intent.putExtra("UserID", order.userId);
			startActivity(intent);
		}
		else
			Toast.makeText(ViewInventory.this, "Enter correct quantities and try again.", Toast.LENGTH_SHORT).show();
	}
}
