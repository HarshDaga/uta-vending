package com.uta.vending;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.InventoryItem;
import com.uta.vending.data.entities.Vehicle;
import com.uta.vending.data.entities.VehicleInventory;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewInventory extends AppCompatActivity {

    long vehicleId;
    Vehicle currentVehicle;
    AppDatabase appDb;
    List<InventoryItem> inventoryItemList;

    private long getIdFromIntent() {
        if (this.getIntent() != null) {
            return this.getIntent().getLongExtra("VehicleID", 0);
        }
        return 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        appDb = AppDatabase.getInstance(this);
        vehicleId = getIdFromIntent();


        if (vehicleId > 0) {
            appDb.vehicleDao().find(vehicleId).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onVehicleGet, this::onLookupError);
        }
    }

    private void onLookupError(Throwable throwable) {

    }

    private void onVehicleGet(Vehicle vehicle) {
        currentVehicle = vehicle;
        appDb.inventoryDao().getInventory(currentVehicle.id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onInventoryGet, this::onLookupError);
    }

    private void onInventoryGet(VehicleInventory vehicleInventory) {
        this.inventoryItemList = vehicleInventory.inventory;

    }
}
