package com.uta.vending;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchVehicle extends AppCompatActivity {

    EditText hoursFrom, hoursTo;
    ListView vehicleList;
    AppDatabase appDb;
    Button btnSearch;
    List<Vehicle> vehicles;

    String filterType;
    String filterLocation;
    long userId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onVehicleListGet(List<Vehicle> vehicles) {

        if (!"All".equals(filterType))
            vehicles = vehicles.stream().filter(vehicle -> vehicle.type.equalsIgnoreCase(filterType)).collect(Collectors.toList());

        if (!"None".equals(filterLocation))
            vehicles = vehicles.stream().filter(vehicle -> vehicle.scheduleToday.location != null && vehicle.scheduleToday.location.equalsIgnoreCase(filterLocation)).collect(Collectors.toList());


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, vehicles.stream().map(vehicle -> vehicle.name + ", " + vehicle.type).collect(Collectors.toList()));
        vehicleList.setAdapter((arrayAdapter));
        vehicleList.invalidate();

        this.vehicles = new ArrayList<Vehicle>(vehicles);
    }

    private void onLookupError(Throwable throwable) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vehicle);
        appDb = AppDatabase.getInstance(this);

        hoursTo = findViewById(R.id.ediTextTimeTo);
        hoursFrom = findViewById(R.id.ediTextTimeFrom);
        vehicleList = findViewById(R.id.listVehicles);
        btnSearch = findViewById(R.id.button);

        userId = getIdFromIntent();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {

                appDb.vehicleDao().getAll().subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(SearchVehicle.this::onVehicleListGet, SearchVehicle.this::onLookupError);
            }

        });

        vehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vehicle vehicle = SearchVehicle.this.vehicles.get(position);
                Intent intent = new Intent(SearchVehicle.this, ViewInventory.class);
                intent.putExtra("VehicleID", vehicle.id);
                intent.putExtra("UserID", userId);
                startActivity(intent);
            }
        });

        Spinner vtype = findViewById(R.id.vtypespinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.VehicleType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vtype.setAdapter(adapter);
        vtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterType = "All";
            }
        });

        Spinner loctn = findViewById(R.id.LocationSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.LocationSp, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loctn.setAdapter(adapter2);
        loctn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterLocation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
