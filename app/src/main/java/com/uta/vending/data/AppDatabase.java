package com.uta.vending.data;

import android.annotation.*;
import android.content.*;
import android.util.*;

import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.*;

import com.uta.vending.data.dao.*;
import com.uta.vending.data.entities.*;

import java.util.*;
import java.util.concurrent.*;

@Database(entities = {User.class, FoodItem.class, Vehicle.class, InventoryItem.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase
{
	private static final String DB_NAME = "data.db";
	private static AppDatabase INSTANCE;
	private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	public static AppDatabase getInstance(@NonNull Context context)
	{
		if (INSTANCE == null)
			synchronized (AppDatabase.class)
			{
				INSTANCE = buildDatabase(context);
				executorService.execute(() -> INSTANCE.runInTransaction(() ->
				{ // dummy tx just to enforce db creation
				}));
			}

		return INSTANCE;
	}

	private static AppDatabase buildDatabase(@NonNull Context context)
	{
		Log.d(AppDatabase.class.getName(), "Creating Database instance");
		return Room
			.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
			.addCallback(new Callback()
			{
				@Override
				public void onCreate(@NonNull SupportSQLiteDatabase db)
				{
					super.onCreate(db);
					Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(context).populate());
				}
			})
			.build();
	}

	abstract public UserDao userDao();

	abstract public FoodDao foodDao();

	abstract public VehicleDao vehicleDao();

	abstract public InventoryDao inventoryDao();

	private void populate()
	{
		Log.d(AppDatabase.class.getName(), "Populating Database");

		populateUsers();
		populateFood();
		populateVehicles();
		populateInventory();
	}

	private void populateUsers()
	{
		User user = new User("Harsh", "Daga",
			"harsh@gmail.com", User.hash("123"),
			null, null,
			Role.USER);

		userDao().insert(user).blockingAwait();
	}

	private void populateFood()
	{
		foodDao()
			.insert
				(
					new FoodItem("Sandwich", 5),
					new FoodItem("Snacks", 3),
					new FoodItem("Drink", 2)
				)
			.blockingAwait();
	}

	private void populateVehicles()
	{
		vehicleDao()
			.insert(
				new Vehicle("Truck 01", "Food Truck"),
				new Vehicle("Truck 02", "Food Truck"),
				new Vehicle("Cart 01", "Cart"),
				new Vehicle("Cart 02", "Cart")
			)
			.blockingAwait();
	}

	@SuppressLint("CheckResult")
	private void populateInventory()
	{
		List<FoodItem> foodItems = foodDao().getAll().blockingFirst();
		List<Vehicle> vehicles = vehicleDao().getAll().blockingFirst();
		for (FoodItem item : foodItems)
			for (Vehicle vehicle : vehicles)
			{
				inventoryDao()
					.insert(new InventoryItem(vehicle.id, item, 5))
					.blockingAwait();
			}
	}
}
