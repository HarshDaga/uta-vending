package com.uta.vending;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.InventoryItem;
import com.uta.vending.data.entities.Order;
import com.uta.vending.data.entities.OrderItem;
import com.uta.vending.data.entities.Vehicle;
import com.uta.vending.data.entities.VehicleInventory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewInventory extends AppCompatActivity {

    public static Order cartOrder;
    public static
    long vehicleId, userId;
    public static Vehicle currentVehicle;
    AppDatabase appDb;
    public static List<InventoryItem> inventoryItemList;
    ListView listInventory;
    Button addToCart;
    EditText editTextDrinks, editTextSnacks, editTextSandwiches;

    private void getIdFromIntent() {
        if (this.getIntent() != null) {
            vehicleId = this.getIntent().getLongExtra("VehicleID", 0);
            userId = this.getIntent().getLongExtra("UserID", 0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        appDb = AppDatabase.getInstance(this);

        editTextSnacks = findViewById(R.id.editTextSnacks);
        editTextDrinks = findViewById(R.id.editTextDrinks);
        editTextSandwiches = findViewById(R.id.editTextSandwiches);

        if (ViewInventory.cartOrder != null) {

            for (OrderItem item : cartOrder.items) {
                if (item.item.type.equals("Sandwich")) {
                    editTextSandwiches.setText(Integer.toString(item.quantity));
                } else if (item.item.type.equals("Snacks")) {
                    editTextSnacks.setText(Integer.toString(item.quantity));
                } else if (item.item.type.equals("Drink")) {
                    editTextDrinks.setText(Integer.toString(item.quantity));
                }
            }

        } else {
            getIdFromIntent();

        }
        listInventory = findViewById(R.id.listInventory);

        addToCart = findViewById(R.id.btnAddToCart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                Integer sandwiches = 0, snacks = 0, drinks = 0;

                try {
                    sandwiches = Integer.parseInt(editTextSandwiches.getText().toString().trim());
                    snacks = Integer.parseInt(editTextSnacks.getText().toString().trim());
                    drinks = Integer.parseInt(editTextDrinks.getText().toString().trim());
                } catch (Exception ex) {
                    Log.e("Error", "Error", ex);
                    Toast.makeText(ViewInventory.this, "Enter correct quantities and try again.", Toast.LENGTH_SHORT).show();
                }
                Order order = new Order(userId, currentVehicle.scheduleToday.operatorId, LocalDateTime.now(), vehicleId, currentVehicle.scheduleToday.location);

                boolean failed = false;

                for (InventoryItem item : ViewInventory.this.inventoryItemList) {

                    if (!failed && item.item.type.equals("Sandwich") && sandwiches > 0) {
                        if (item.quantity - sandwiches >= 0) {

                            order.addItem(new OrderItem(item.item, sandwiches));
                        } else
                            failed = true;
                    }
                    if (!failed && item.item.type.equals("Snacks") && snacks > 0) {
                        if (item.quantity - snacks >= 0) {
                            order.addItem(new OrderItem(item.item, snacks));
                        } else
                            failed = true;
                    }
                    if (!failed && item.item.type.equals("Drink") && drinks > 0) {
                        if (item.quantity - drinks >= 0) {
                            order.addItem(new OrderItem(item.item, drinks));
                        } else
                            failed = true;
                    }
                }

                if (!failed) {
                    order.isServed = false;
                    cartOrder = order;
                    Intent intent = new Intent(ViewInventory.this, EditCart.class);
                    intent.putExtra("UserID", order.userId);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(ViewInventory.this, "Enter correct quantities and try again.", Toast.LENGTH_SHORT).show();


            }
        });

        if (vehicleId > 0) {
            appDb.vehicleDao().find(vehicleId).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onVehicleGet, this::onLookupError);
        }
    }

    private void onLookupError(Throwable throwable) {
        Log.e("ViewInventory", "Error", throwable);
        Toast.makeText(ViewInventory.this, "Error", Toast.LENGTH_LONG).show();
    }

    private void onVehicleGet(Vehicle vehicle) {
        currentVehicle = vehicle;
        appDb.inventoryDao().getInventory(currentVehicle.id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onInventoryGet, this::onLookupError);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onInventoryGet(VehicleInventory vehicleInventory) {
        this.inventoryItemList = vehicleInventory.inventory;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vehicleInventory.inventory.stream().map(item -> "Item: " + item.item.type + ", Cost: $" + String.format("%.2f", item.item.cost.doubleValue()) + ", Quantity: " + item.quantity).collect(Collectors.toList()));
        listInventory.setAdapter(arrayAdapter);
        listInventory.invalidate();
    }
}
