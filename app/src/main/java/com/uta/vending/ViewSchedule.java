package com.uta.vending;

import android.annotation.*;
import android.os.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import java.time.format.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class ViewSchedule extends AppCompatActivity
{
    TextView editTodayVehName, editTodayLoc, editTodayTime;
    TextView editNextVehName, editNextLoc, editNextTime;
    AppDatabase appDb;

    long opId;

    private long getIdFromIntent()
    {
        return this.getIntent() == null
            ? 0
            : this.getIntent().getLongExtra("ID", 0);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        editTodayVehName = findViewById(R.id.EditTextSchCurVeh);
        editTodayLoc = findViewById(R.id.EditTextSchCurLoc);
        editTodayTime = findViewById(R.id.EditTextSchCurTime);
        editNextVehName = findViewById(R.id.EditTextSchNextVeh);
        editNextLoc = findViewById(R.id.EditTextSchNextLoc);
        editNextTime = findViewById(R.id.EditTextSchNextTime);

        appDb = AppDatabase.getInstance(this);

        opId = getIdFromIntent();

        appDb.vehicleDao()
            .findForOperatorToday(opId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onTodayFound, this::onTodayNotFound);

        appDb.vehicleDao()
            .findForOperatorNext(opId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onNextFound, this::onNextNotFound);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onTodayFound(Vehicle vehicle)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        editTodayVehName.setText(vehicle.name);
        String location = vehicle.scheduleToday.location;
        if (location == null)
        {
            editTodayTime.setText("");
            editTodayLoc.setText("None");
        }
        else
        {
            editTodayLoc.setText(location);
            editTodayTime.setText(vehicle.scheduleToday.start.format(formatter) + " - " + vehicle.scheduleToday.end.format(formatter));
        }
    }

    @SuppressLint("SetTextI18n")
    private void onTodayNotFound(Throwable throwable)
    {
        editTodayVehName.setText("None");
        editTodayLoc.setText("None");
        editTodayTime.setText("");
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onNextFound(Vehicle vehicle)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        editNextVehName.setText(vehicle.name);
        String location = vehicle.scheduleNext.location;
        if (location == null)
        {
            editNextTime.setText("");
            editNextLoc.setText("None");
        }
        else
        {
            editNextLoc.setText(location);
            editNextTime.setText(vehicle.scheduleNext.start.format(formatter) + " - " + vehicle.scheduleNext.end.format(formatter));
        }
    }

    @SuppressLint("SetTextI18n")
    private void onNextNotFound(Throwable throwable)
    {
        editNextVehName.setText("None");
        editNextLoc.setText("None");
        editNextTime.setText("");
    }
}
