package com.uta.vending;

import android.annotation.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;
import com.uta.vending.utilities.*;

import java.time.format.*;
import java.util.*;
import java.util.stream.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class ViewVehicle extends AppCompatActivity
{
	ListView listVehicles;
	TextView vehicleName;
	Spinner spinnerLocations;
	Button btnAssignLocation;
	TextView editTextTimeFrom, editTextTimeTo;
	AppDatabase appDb;

	String selectedLocation = "None";
	Vehicle selectedVehicle;
	TimeChooser fromTime, toTime;
	List<Vehicle> vehicles;
	HashMap<Long, User> operators;

	@RequiresApi(api = Build.VERSION_CODES.O)
	private static String vehicleToString(Vehicle vehicle)
	{
		String result = vehicle.name;
		if (vehicle.scheduleNext.location == null)
			return result + ", Location: None";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

		return vehicle.name +
			", Location: " + vehicle.scheduleNext.location +
			", Time: " + vehicle.scheduleNext.start.format(formatter) + " - " + vehicle.scheduleNext.end.format(formatter);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_vehicle);

		appDb = AppDatabase.getInstance(this);
		listVehicles = findViewById(R.id.listViewVehicles);
		vehicleName = findViewById(R.id.TextViewVehicleName);
		spinnerLocations = findViewById(R.id.SpinnerLocation);
		editTextTimeFrom = findViewById(R.id.editTextTimeFrom);
		editTextTimeTo = findViewById(R.id.editTextTimeTo);
		btnAssignLocation = findViewById(R.id.AssignLocation);
		btnAssignLocation.setOnClickListener(this::onClickAssignLocation);

		listVehicles.setOnItemClickListener(this::onSelectVehicle);
		fromTime = new TimeChooser(editTextTimeFrom, this);
		toTime = new TimeChooser(editTextTimeTo, this);

		ArrayAdapter<CharSequence> locations = ArrayAdapter.createFromResource(
			this,
			R.array.LocationSp,
			android.R.layout.simple_spinner_item
		);
		locations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLocations.setAdapter(locations);
		spinnerLocations.setSelection(0);
		spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				selectedLocation = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});

		appDb.userDao()
			.getAll(Role.OPERATOR)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchOperators);

		appDb.vehicleDao()
			.getAll()
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchVehicles);
	}

	private void onSelectVehicle(AdapterView<?> adapterView, View view, int position, long id)
	{
		Vehicle vehicle = this.vehicles.get(position);
		selectedVehicle = vehicle;
		vehicleName.setText(vehicle.name);
	}

	private void onFetchOperators(List<User> users)
	{
		operators = new HashMap<>();
		for (User x : users)
			operators.put(x.id, x);
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	private void onFetchVehicles(List<Vehicle> vehicles)
	{
		this.vehicles = vehicles;

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
			this,
			android.R.layout.simple_list_item_1,
			vehicles.stream()
				.map(ViewVehicle::vehicleToString)
				.collect(Collectors.toList())
		);
		listVehicles.setAdapter(arrayAdapter);
		listVehicles.invalidate();
	}

	@SuppressLint("CheckResult")
	@RequiresApi(api = Build.VERSION_CODES.O)
	private void onClickAssignLocation(View v)
	{
		if (selectedVehicle == null)
		{
			showToast("ERROR: No vehicle selected");
			return;
		}

		if (selectedLocation.equals("None"))
		{
			selectedVehicle.scheduleNext.location = null;
			selectedVehicle.scheduleNext.start = null;
			selectedVehicle.scheduleNext.end = null;
		}
		else
		{
			selectedVehicle.scheduleNext.location = selectedLocation;
			if (!fromTime.isTimeSet() || !toTime.isTimeSet())
			{
				showToast("ERROR: Time slots not set");
				return;
			}
			selectedVehicle.scheduleNext.start = fromTime.getTime();
			selectedVehicle.scheduleNext.end = toTime.getTime();
		}
		appDb.vehicleDao()
			.update(selectedVehicle)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onUpdateVehicle);
	}

	@SuppressLint("CheckResult")
	private void onUpdateVehicle()
	{
		appDb.vehicleDao()
			.getAll()
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onFetchVehicles);
	}

	private void showToast(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
