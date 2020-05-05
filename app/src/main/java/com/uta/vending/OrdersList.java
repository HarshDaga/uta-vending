package com.uta.vending;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.Order;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class OrdersList extends AppCompatActivity {

    ListView lstOrders;
    List<Order> listOrders;
    long userId;
    AppDatabase appDb;

    private long getOrderIdFromIntent() {
        if (this.getIntent() != null) {
            return this.getIntent().getLongExtra("UserID", 0);
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);
        appDb = AppDatabase.getInstance(this);
        userId = getOrderIdFromIntent();

        appDb.orderDao().getAll(userId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orders -> {
                    OrdersList.this.listOrders = orders;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                OrdersList.this,
                                android.R.layout.simple_list_item_1,
                                orders
                                        .stream()
                                        .map(o -> "Order ID: " + o.id +

                                                "    Cost: $" + String.format("%.2f", o.cost.doubleValue()) +
                                                "    (" + o.location + ")").collect(Collectors.toList())
                        );
                        lstOrders.setAdapter(arrayAdapter);
                        lstOrders.invalidate();
                    }
                });

        lstOrders = findViewById(R.id.listOrder);
        lstOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrdersList.this, OrderDetails.class);
                intent.putExtra("OrderID", OrdersList.this.listOrders.get(position).id);
                startActivity(intent);
            }
        });

    }
}
