package com.uta.vending;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;
import com.uta.vending.utilities.*;

import java.util.*;
import java.util.stream.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class SearchVehicle extends AppCompatActivity
{
	EditText hoursFrom, hoursTo;
	ListView vehicleList;
	AppDatabase appDb;
	Button btnSearch;
	List<Vehicle> vehicles;

	String filterType;
	String filterLocation;
	TimeChooser fromTime, toTime;
	long userId;

	@RequiresApi(api = Build.VERSION_CODES.O)
	private void onVehicleListGet(List<Vehicle> vehicles)
	{
		vehicles = vehicles.stream()
			.filter(vehicle -> vehicle.scheduleToday.location != null)
			.collect(Collectors.toList());

		if (!"All".equals(filterType))
			vehicles = vehicles.stream()
				.filter(vehicle -> vehicle.type.equalsIgnoreCase(filterType))
				.collect(Collectors.toList());

		if (!"All".equals(filterLocation))
			vehicles = vehicles.stream()
				.filter(vehicle -> vehicle.scheduleToday.location.equalsIgnoreCase(filterLocation))
				.collect(Collectors.toList());

		vehicles = vehicles.stream()
			.filter(this::checkVehicleTime)
			.collect(Collectors.toList());

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			this,
			android.R.layout.simple_list_item_1,
			vehicles.stream()
				.map(vehicle -> vehicle.name + ", " + vehicle.type)
				.collect(Collectors.toList())
		);
		vehicleList.setAdapter(arrayAdapter);
		vehicleList.invalidate();

		this.vehicles = new ArrayList<>(vehicles);
	}

	private void onLookupError(Throwable throwable)
	{
		Log.e("SearchVehicle", "Error", throwable);
		Toast.makeText(SearchVehicle.this, "Error", Toast.LENGTH_LONG).show();
	}

	private long getIdFromIntent()
	{
		if (this.getIntent() != null)
		{
			return this.getIntent().getLongExtra("ID", 0);
		}
		return 0;
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_vehicle);
		appDb = AppDatabase.getInstance(this);

		hoursTo = findViewById(R.id.editTextTo);
		hoursFrom = findViewById(R.id.editTextFrom);
		vehicleList = findViewById(R.id.listVehicles);
		btnSearch = findViewById(R.id.btnSearchVehicle);

		userId = getIdFromIntent();
		btnSearch.setOnClickListener(this::onClickSearch);
		vehicleList.setOnItemClickListener(this::onClickVehicle);

		fromTime = new TimeChooser(hoursFrom, this);
		toTime = new TimeChooser(hoursTo, this);

		Spinner vehTypeSpinner = findViewById(R.id.SpinnerVehType);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
			this,
			R.array.VehicleType,
			android.R.layout.simple_spinner_item
		);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		vehTypeSpinner.setAdapter(adapter);
		vehTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				filterType = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				filterType = "All";
			}
		});

		Spinner locationSpinner = findViewById(R.id.SpinnerLocation);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
			this,
			R.array.LocationSp,
			android.R.layout.simple_spinner_item
		);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(adapter2);
		locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				filterLocation = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
	}

	private void onClickVehicle(AdapterView<?> parent, View view, int position, long id)
	{
		Vehicle vehicle = SearchVehicle.this.vehicles.get(position);
		Intent intent = new Intent(SearchVehicle.this, ViewInventory.class);
		intent.putExtra("VehicleID", vehicle.id);
		intent.putExtra("UserID", userId);
		startActivity(intent);
	}

	@SuppressLint("CheckResult")
	private void onClickSearch(View v)
	{
		appDb.vehicleDao()
			.getAll()
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(SearchVehicle.this::onVehicleListGet, SearchVehicle.this::onLookupError);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private boolean checkVehicleTime(Vehicle vehicle)
	{
		if (fromTime.isTimeSet() && toTime.isTimeSet())
			return vehicle.scheduleToday.containsTime(fromTime.getTime()) ||
				vehicle.scheduleToday.containsTime(toTime.getTime());

		if (fromTime.isTimeSet())
			return vehicle.scheduleToday.containsTime(fromTime.getTime());

		if (toTime.isTimeSet())
			return vehicle.scheduleToday.containsTime(toTime.getTime());

		return true;
	}
}
