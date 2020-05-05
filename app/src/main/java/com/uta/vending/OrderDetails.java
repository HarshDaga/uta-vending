package com.uta.vending;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.Order;

import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderDetails extends AppCompatActivity {


    ListView lstOrders;
    Order order;
    long orderId;
    AppDatabase appDb;
    TextView cost;

    private long getOrderIdFromIntent() {
        if (this.getIntent() != null) {
            return this.getIntent().getLongExtra("OrderID", 0);
        }
        return 0;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDb = AppDatabase.getInstance(this);
        orderId = getOrderIdFromIntent();
        cost = findViewById(R.id.textViewCost);
        lstOrders = findViewById(R.id.listOrderItems);
//
//        lstOrders.

        appDb.orderDao()
                .findOrder(orderId)
                .subscribeOn(Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(order1 -> {

                    order = order1;
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(OrderDetails.this, android.R.layout.simple_list_item_1, order1.items.stream().map(item -> ("Item: " + item.item.type + ", Cost: $" + String.format("%.2f", item.item.cost.doubleValue()) + ", Quantity: " + item.quantity)).collect(Collectors.toList()));
                    lstOrders.setAdapter(arrayAdapter);
                    lstOrders.invalidate();
                    cost.append(" " + String.format("%.2f", order1.cost.doubleValue()));
                });
        setContentView(R.layout.activity_order_details);
    }
}
