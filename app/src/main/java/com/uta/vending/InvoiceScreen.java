package com.uta.vending;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.Order;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InvoiceScreen extends AppCompatActivity {

    ListView lstOrders;
    Order order;
    long orderId;
    AppDatabase appDb;
    TextView cost;

    EditText orderID, date, location;

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
        orderId = getOrderIdFromIntent();
        setContentView(R.layout.activity_invoice_screen);
        cost = findViewById(R.id.textViewCost2);
        lstOrders = findViewById(R.id.listOrderItems2);
        appDb = AppDatabase.getInstance(this);

        orderID = findViewById(R.id.editTextOderID2);
        date = findViewById(R.id.editTextDate);
        location = findViewById(R.id.editTextLocation);


        appDb.orderDao()
                .findOrder(orderId)
                .subscribeOn(Schedulers.computation()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(order1 -> {

                    order = order1;
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(InvoiceScreen.this, android.R.layout.simple_list_item_1, Arrays.stream(order1.items).map(item -> ("Item: " + item.item.type + ", Cost: $" + String.format("%.2f", item.getTotalCost().doubleValue()) + ", Quantity: " + item.quantity)).collect(Collectors.toList()));
                    lstOrders.setAdapter(arrayAdapter);
                    lstOrders.invalidate();
                    cost.append(" " + String.format("%.2f", order1.cost.doubleValue()));

                    orderID.setText(order.id + "");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        date.setText(order.date.format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
                    }

                    location.setText(order.location);

                });

    }
}
