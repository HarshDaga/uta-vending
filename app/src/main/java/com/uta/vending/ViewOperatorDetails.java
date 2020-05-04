package com.uta.vending;

import android.annotation.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

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
    AppDatabase appDb;

    String chosenVehicle;
    User operator;

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

        opId = getIdFromIntent();

        textViewUserName = findViewById(R.id.TextViewUserName);
        textViewOpFirstName = findViewById(R.id.TextViewOpFirstName);
        textViewOpLastName = findViewById(R.id.TextViewOpLastName);
        textViewNumTx = findViewById(R.id.TextViewNumTx);
        textViewCurVeh = findViewById(R.id.TextViewCurVeh);
        textViewNextVeh = findViewById(R.id.TextViewNextVeh);
        btnAssignOperator = findViewById(R.id.BtnAssignOperator);

        btnAssignOperator.setOnClickListener(this::onClickAssign);

        spinnerVehicleToAssign = findViewById(R.id.SpinnerVehicleToAssign);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.MVehicleNameSp, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicleToAssign.setAdapter(adapter4);
        spinnerVehicleToAssign.setOnItemSelectedListener(this);

        init();
    }

    @SuppressLint("CheckResult")
    private void init()
    {
        appDb.userDao().getUser(opId)
            .subscribe(this::onFetchOperator);
    }

    @SuppressLint("CheckResult")
    private void onClickAssign(View view)
    {
        appDb.vehicleDao()
            .find(chosenVehicle)
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
        vehicle.scheduleNext.operatorId = opId;

        appDb.vehicleDao()
            .update(vehicle)
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
            .subscribe(
                x -> textViewCurVeh.setText(x.name),
                x -> textViewCurVeh.setText("None")
            );
        appDb.vehicleDao()
            .findForOperatorNext(opId)
            .subscribe(
                x -> textViewNextVeh.setText(x.name),
                x -> textViewNextVeh.setText("None")
            );
        appDb.orderDao()
            .getTxCount(opId)
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
}
