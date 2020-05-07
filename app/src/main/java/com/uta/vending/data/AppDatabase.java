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

import java.time.*;
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
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "data.db";
    private static AppDatabase INSTANCE;
    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static AppDatabase getInstance(@NonNull Context context) {
        if (INSTANCE == null)
            synchronized (AppDatabase.class) {
                INSTANCE = buildDatabase(context);
                executorService.execute(() -> INSTANCE.runInTransaction(() ->
                { // dummy tx just to enforce db creation
                }));
            }

        return INSTANCE;
    }

    private static AppDatabase buildDatabase(@NonNull Context context) {
        Log.d(AppDatabase.class.getName(), "Creating Database instance");
        return Room
                .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                .addCallback(new Callback() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
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
    private void populate() {
        Log.d(AppDatabase.class.getName(), "Populating Database");

        populateUsers();
        populateFood();
        populateVehicles();
        populateInventory();

        populateOrders();
    }

    private void populateUsers() {
        User user = new User("Harsh", "Daga",
                "harsh@gmail.com", User.hash("123"),
            null, new Address(),
                Role.USER);

        userDao().insert(user).blockingAwait();

        for (Role role : Role.values())
            for (int i = 0; i < 5; i++) {
                user.firstName = role.toString().substring(0, 1) + (i + 1);
                user.lastName = "Empty";
                user.email = user.firstName + "@e.c";
                user.role = role;
                user.password = User.hash("123");

                userDao().insert(user).blockingAwait();
            }
    }

    private void populateFood() {
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
        LocalDateTime today = LocalDateTime.now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        Vehicle[] vehicles = new Vehicle[]
                {
                        new Vehicle("Truck 01", "Food Truck"),
                        new Vehicle("Truck 02", "Food Truck"),
                        new Vehicle("Cart 01", "Cart"),
                        new Vehicle("Cart 02", "Cart")
                };
        vehicles[0].scheduleToday.location = "Cooper & UTA Blvd";
        vehicles[0].scheduleToday.start = today.withHour(11);
        vehicles[0].scheduleToday.end = today.withHour(13);
        vehicles[1].scheduleToday.location = "S Davis & W Mitchell";
        vehicles[1].scheduleToday.start = today.withHour(14);
        vehicles[1].scheduleToday.end = today.withHour(17);

        User operator = userDao().getUser("O1@e.c", Role.OPERATOR).blockingGet();
        vehicles[0].scheduleToday.operatorId = operator.id;
        vehicles[1].scheduleToday.operatorId = operator.id;

        vehicleDao()
                .insert(vehicles)
                .blockingAwait();
    }
    @SuppressLint("CheckResult")
    private void populateInventory() {
        List<FoodItem> foodItems = foodDao().getAll().blockingFirst();
        List<Vehicle> vehicles = vehicleDao().getAll().blockingFirst();
        for (FoodItem item : foodItems)
            for (Vehicle vehicle : vehicles) {
                inventoryDao()
                        .insert(new InventoryItem(vehicle.id, item, 200))
                        .blockingAwait();
            }
    }

    @SuppressWarnings("unused")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void populateOrders() {
        Order order = new Order(2, 7, LocalDateTime.now(), 1, "UTA");
        List<FoodItem> foodItems = foodDao().getAll().blockingFirst();
        order.addItem(new OrderItem(foodItems.get(0), 2));
        order.addItem(new OrderItem(foodItems.get(1), 1));

        long id = orderDao().insert(order).blockingGet();
        order = orderDao().findOrder(1).blockingGet();
    }
}
