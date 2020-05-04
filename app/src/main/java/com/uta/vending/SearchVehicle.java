package com.uta.vending;

import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;

public class SearchVehicle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vehicle);

        Button btnviewinventory=findViewById(R.id.ViewInventory);
        btnviewinventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invscreen=new Intent(SearchVehicle.this, ViewInventory.class);
                startActivity(invscreen);
            }
        });

        Spinner vtype=findViewById(R.id.vtypespinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.VehicleType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vtype.setAdapter(adapter);
        vtype.setOnItemSelectedListener(this);

	    Spinner loctn = findViewById(R.id.LocationSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.LocationSp, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loctn.setAdapter(adapter2);
        loctn.setOnItemSelectedListener(this);

        Spinner vehname=findViewById(R.id.vnamespinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.VehicleNameSp, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehname.setAdapter(adapter3);
        vehname.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String vtext = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
