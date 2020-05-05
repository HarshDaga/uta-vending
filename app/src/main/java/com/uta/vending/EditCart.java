package com.uta.vending;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.InventoryItem;
import com.uta.vending.data.entities.Order;
import com.uta.vending.data.entities.OrderItem;

import java.util.Arrays;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditCart extends AppCompatActivity {

    Button btncheckout;


    ListView lstOrders;
    Order order;

    AppDatabase appDb;
    TextView cost;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart);
        order = ViewInventory.cartOrder;
        lstOrders = findViewById(R.id.listOrderItems1);
        cost = findViewById(R.id.textViewCost1);
        appDb = AppDatabase.getInstance(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditCart.this, android.R.layout.simple_list_item_1, Arrays.stream(order.items).map(item -> ("Item: " + item.item.type + ", Cost: $" + String.format("%.2f", item.item.cost.doubleValue()) + ", Quantity: " + item.quantity)).collect(Collectors.toList()));
        lstOrders.setAdapter(arrayAdapter);
        lstOrders.invalidate();
        cost.append(" " + String.format("%.2f", order.cost.doubleValue()));

        btncheckout = findViewById(R.id.btnCheckout1);
        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appDb.orderDao().insert(order)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {

                            appDb.vehicleDao().getInventory(order.vehicleId)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(vehicleInventory -> {
//                                        vehicleInventory.inventory
                                        ViewInventory.cartOrder = null;
                                        for (OrderItem orderItem : order.items) {

                                            vehicleInventory.inventory.
                                                    stream().
                                                    filter(inventoryItem ->
                                                            inventoryItem.item.type.equals(orderItem.item.type))
                                                    .collect(Collectors.toList()).get(0).quantity -= orderItem.quantity;

                                        }

                                        InventoryItem[] items = new InventoryItem[3];
                                        items = vehicleInventory.inventory.toArray(items);

                                        appDb.inventoryDao().update(items).subscribeOn(Schedulers.computation())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    Intent invscreen = new Intent(EditCart.this, InvoiceScreen.class);
                                                    invscreen.putExtra("OrderID", order.id);
                                                    startActivity(invscreen);
                                                });


                                    });


                        });


            }
        });
    }
}
