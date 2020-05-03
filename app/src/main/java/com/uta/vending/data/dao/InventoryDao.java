package com.uta.vending.data.dao;

import androidx.room.*;

import com.uta.vending.data.entities.*;

import io.reactivex.*;

@Dao
public interface InventoryDao
{
	@Insert
	Completable insert(InventoryItem... items);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	Completable update(InventoryItem... items);

	@Delete
	Completable delete(InventoryItem... items);

	@Transaction
	@Query("SELECT * FROM vehicles WHERE id = :id LIMIT 1")
	Single<VehicleInventory> getInventory(long id);
}

