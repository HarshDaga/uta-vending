package com.uta.vending;

import android.annotation.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import java.util.*;
import java.util.stream.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class ViewOperatorDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    long opId;
    TextView textViewUserName;
    TextView textViewOpFirstName;
    TextView textViewOpLastName;
    TextView textViewNumTx;
    TextView textViewCurVeh;
    TextView textViewNextVeh;
    Spinner spinnerVehicleToAssign;
    Button btnAssignOperator;
    Button btnUnassignOperator;
    AppDatabase appDb;

    String chosenVehicle;
    User operator;
    Vehicle vehicleNext;
    List<Vehicle> vehicles;

    private long getIdFromIntent()
    {
        if (this.getIntent() != null)
        {
            return this.getIntent().getLongExtra("OP_ID", 0);
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_operator_details);

        appDb = AppDatabase.getInstance(this);

        opId = getIdFromIntent();

        textViewUserName = findViewById(R.id.TextViewUserName);
        textViewOpFirstName = findViewById(R.id.TextViewOpFirstName);
        textViewOpLastName = findViewById(R.id.TextViewOpLastName);
        textViewNumTx = findViewById(R.id.TextViewNumTx);
        textViewCurVeh = findViewById(R.id.TextViewCurVeh);
        textViewNextVeh = findViewById(R.id.TextViewNextVeh);
        btnAssignOperator = findViewById(R.id.BtnAssignOperator);
        btnUnassignOperator = findViewById(R.id.BtnUnassignOperator);

        btnAssignOperator.setOnClickListener(this::onClickAssign);
        btnUnassignOperator.setOnClickListener(this::onClickUnassign);

        spinnerVehicleToAssign = findViewById(R.id.SpinnerVehicleToAssign);

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onFetchVehicles(List<Vehicle> vehicles)
    {
        this.vehicles = vehicles;
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            vehicles.stream()
                .filter(v -> v.scheduleNext.operatorId != opId)
                .map(v -> v.name)
                .collect(Collectors.toList())
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicleToAssign.setAdapter(arrayAdapter);
        spinnerVehicleToAssign.setOnItemSelectedListener(this);
    }

    @SuppressLint("CheckResult")
    private void init()
    {
        appDb.userDao()
            .getUser(opId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onFetchOperator);

        appDb.vehicleDao()
            .getAll()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onFetchVehicles);
    }

    @SuppressLint("CheckResult")
    private void onClickAssign(View view)
    {
        appDb.vehicleDao()
            .find(chosenVehicle)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onVehicleFound);
    }

    private void onVehicleFound(Vehicle vehicle)
    {
        if (vehicle.scheduleNext.operatorId != 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ViewOperatorDetails.this);
            builder.setTitle("Reassign?");
            builder.setMessage("This vehicle was already assigned to another operator. Reassign?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (d, w) -> assignVehicle(vehicle));
            builder.setNegativeButton("No", null);

            builder.show();
            return;
        }

        assignVehicle(vehicle);
    }

    @SuppressLint("CheckResult")
    private void assignVehicle(Vehicle vehicle)
    {
        Vehicle[] toUpdate;
        if (vehicleNext != null)
        {
            vehicleNext.scheduleNext.operatorId = 0;
            toUpdate = new Vehicle[2];
            toUpdate[1] = vehicleNext;
        }
        else
            toUpdate = new Vehicle[1];
        vehicle.scheduleNext.operatorId = opId;
        toUpdate[0] = vehicle;

        appDb.vehicleDao()
            .update(toUpdate)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::init);

        Toast.makeText(ViewOperatorDetails.this, "Operator assigned to vehicle", Toast.LENGTH_LONG).show();
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void onFetchOperator(User operator)
    {
        this.operator = operator;
        textViewUserName.setText(operator.email.split("@", 2)[0]);
        textViewOpFirstName.setText(operator.firstName);
        textViewOpLastName.setText(operator.lastName);
        appDb.vehicleDao()
            .findForOperatorToday(opId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                x -> textViewCurVeh.setText(x.name),
                x -> textViewCurVeh.setText("None")
            );
        appDb.vehicleDao()
            .findForOperatorNext(opId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                x ->
                {
                    this.vehicleNext = x;
                    textViewNextVeh.setText(x.name);
                },
                x -> textViewNextVeh.setText("None")
            );
        appDb.orderDao()
            .getTxCount(opId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                x -> textViewNumTx.setText(x.toString()),
                x -> textViewNumTx.setText("-")
            );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        chosenVehicle = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @SuppressLint("CheckResult")
    private void onClickUnassign(View view)
    {
        if (vehicleNext == null)
            return;

        vehicleNext.scheduleNext.operatorId = 0;

        appDb.vehicleDao()
            .update(vehicleNext)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::init);

        Toast.makeText(ViewOperatorDetails.this, "Operator unassigned", Toast.LENGTH_LONG).show();
    }
}
