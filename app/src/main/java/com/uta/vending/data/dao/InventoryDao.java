package com.uta.vending.data.dao;

import androidx.room.*;

import com.uta.vending.data.entities.*;

import io.reactivex.*;

@Dao
public interface InventoryDao
{
	@Insert
	Completable insert(InventoryItem... item);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	Completable update(InventoryItem... item);

	@Delete
	Completable delete(InventoryItem... item);
}
