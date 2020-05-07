package com.uta.vending.data;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.*;
import androidx.room.*;
import androidx.sqlite.db.*;

import com.uta.vending.data.dao.*;
import com.uta.vending.data.entities.*;

import java.util.*;
import java.util.concurrent.*;

@Database
    (
        entities =
            {
                User.class, FoodItem.class,
                Vehicle.class, InventoryItem.class,
                Order.class, Revenue.class
            },
        version = 9
    )
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
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db)
                {
                    super.onCreate(db);
                    Executors.newSingleThreadScheduledExecutor().execute(() -> getInstance(context).populate());
                }
            })
            .fallbackToDestructiveMigration()
            .build();
    }

    abstract public UserDao userDao();

    abstract public FoodDao foodDao();

    abstract public VehicleDao vehicleDao();

    abstract public InventoryDao inventoryDao();

    abstract public OrderDao orderDao();

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        User harsh = new User(
            "Harsh", "Daga",
            "harsh@mavs.uta.edu",
            User.hash("Password123$%^"),
            "123-456-7890", new Address("Crooked St", "Arlington", "Tx", "76010"),
            Role.USER
        );
        User sam = new User(
            "Sam", "Johnson",
            "sammyJ@mavs.uta.edu",
            User.hash("Password12$"),
            "817-777-2000", new Address("Anywhere Ln", "Arlington", "Tx", "76019"),
            Role.MANAGER
        );
        User susan = new User(
            "Susan", "Queen",
            "SuzieQ@mavs.uta.edu",
            User.hash("Password$34"),
            "817-777-2345", new Address("Straight Dr", "Arlington", "Tx", "76019"),
            Role.OPERATOR
        );

        userDao()
            .insert(harsh, sam, susan)
            .blockingAwait();
    }

    private void populateFood()
    {
        foodDao()
            .insert
                (
                    new FoodItem("Sandwich", 5.75),
                    new FoodItem("Snacks", 1.25),
                    new FoodItem("Drink", 1.5)
                )
            .blockingAwait();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void populateVehicles()
    {
        Vehicle[] vehicles = new Vehicle[7];
        for (int i = 0; i < 2; i++)
            vehicles[i] = new Vehicle("Truck " + (i + 1), "Food Truck");
        for (int i = 0; i < 5; i++)
            vehicles[i + 2] = new Vehicle("Cart " + (i + 1), "Cart");

        vehicleDao()
            .insert(vehicles)
            .blockingAwait();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    private void populateInventory()
    {
        List<FoodItem> foodItems = foodDao().getAll().blockingFirst();
        List<Vehicle> vehicles = vehicleDao().getAll().blockingFirst();

        FoodItem sand = foodItems.stream().filter(x -> x.type.equals("Sandwich")).findFirst().get();
        FoodItem snack = foodItems.stream().filter(x -> x.type.equals("Snacks")).findFirst().get();
        FoodItem drink = foodItems.stream().filter(x -> x.type.equals("Drink")).findFirst().get();

        for (Iterator<Vehicle> it = vehicles.stream().filter(x -> x.type.equals("Cart")).iterator(); it.hasNext(); )
        {
            Vehicle vehicle = it.next();
            inventoryDao()
                .insert(
                    new InventoryItem(vehicle.id, sand, 5),
                    new InventoryItem(vehicle.id, snack, 30),
                    new InventoryItem(vehicle.id, drink, 30)
                )
                .blockingAwait();
        }

        for (Iterator<Vehicle> it = vehicles.stream().filter(x -> x.type.equals("Food Truck")).iterator(); it.hasNext(); )
        {
            Vehicle vehicle = it.next();
            inventoryDao()
                .insert(
                    new InventoryItem(vehicle.id, sand, 35),
                    new InventoryItem(vehicle.id, snack, 40),
                    new InventoryItem(vehicle.id, drink, 50)
                )
                .blockingAwait();
        }
    }
}
