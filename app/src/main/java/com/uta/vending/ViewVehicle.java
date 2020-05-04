package com.uta.vending;

import android.annotation.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import java.time.*;
import java.util.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class ViewVehicle extends AppCompatActivity
{
	Button btnAssignLocation;
	AppDatabase appDb;
	private List<Vehicle> vehicles;
	private HashMap<Long, User> operators;

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_vehicle);

		appDb = AppDatabase.getInstance(this);
		btnAssignLocation = findViewById(R.id.AssignLocation);
		btnAssignLocation.setOnClickListener(this::onClickAssignLocation);

		appDb.userDao().getAll(Role.OPERATOR)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchOperators);

		appDb.vehicleDao().getAll()
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchVehicles);
	}

	private void onFetchOperators(List<User> users)
	{
		operators = new HashMap<>();
		for (User x : users)
			operators.put(x.id, x);
	}

	private void onFetchVehicles(List<Vehicle> vehicles)
	{
		this.vehicles = vehicles;

		// TODO: Update vehicle list on UI using `schedules`
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	private void onClickAssignLocation(View v)
	{
		// TODO replace with UI input
		String vehicleName = "";
		String location = "";
		LocalDateTime start = null, end = null;

		Vehicle vehicle = vehicles
			.stream()
			.filter(x -> x.name.equalsIgnoreCase(vehicleName))
			.findAny().orElse(null);

		if (vehicle == null)
			return;

		vehicle.scheduleNext.location = location;
		vehicle.scheduleNext.start = start;
		vehicle.scheduleNext.end = end;
		appDb.vehicleDao().update(vehicle).subscribe();
	}
}
