package com.uta.vending;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class ProcessOrder extends AppCompatActivity
{
	EditText etOrderId;
	EditText etLastName;
	Button btnProcessOrder;
	AppDatabase appDb;
	private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_process_order);
	    etOrderId = findViewById(R.id.editTextProcessOrderId);
	    etLastName = findViewById(R.id.editTextProcessOrderLastName);
	    appDb = AppDatabase.getInstance(this);

	    btnProcessOrder = findViewById(R.id.processorder);
	    btnProcessOrder.setOnClickListener(this::onClickProcess);
    }

	@SuppressLint("CheckResult")
	private void onClickProcess(View v)
	{
		long orderId = Long.parseLong(etOrderId.getText().toString().trim());

		appDb.orderDao()
			.findOrder(orderId)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onOrderFound, this::onOrderLookupError);
	}

	@SuppressLint("CheckResult")
	private void onOrderFound(Order order)
	{
		this.order = order;
		if (order.isServed)
		{
			showToast("Order already served");
			return;
		}

		appDb.userDao()
			.getUser(order.userId)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchUser);
	}

	private void onFetchUser(User user)
	{
		String lastName = etLastName.getText().toString().trim();

		if (!user.lastName.equalsIgnoreCase(lastName))
		{
			showToast("Order and last name mismatch");
			return;
		}

		processOrder();
	}

	@SuppressLint("CheckResult")
	private void processOrder()
	{
		Intent intent = new Intent(ProcessOrder.this, OrderDetails.class);
		intent.putExtra("OrderID", order.id);
		startActivity(intent);
	}

	private void onOrderLookupError(Throwable throwable)
	{
		showToast("No such order found");
	}

	private void showToast(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
