package com.uta.vending.data.dao;

import androidx.room.*;

import com.uta.vending.data.entities.*;

import java.util.*;

import io.reactivex.*;

@SuppressWarnings("unused")
@Dao
public interface VehicleDao
{
	@Insert
	Completable insert(Vehicle... vehicle);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	Completable update(Vehicle... vehicle);

	@Query("SELECT * FROM vehicles")
	Flowable<List<Vehicle>> getAll();

	@Query("SELECT * FROM vehicles WHERE name = :name LIMIT 1")
	Single<Vehicle> find(String name);

	@Query("SELECT * FROM vehicles WHERE id = :id LIMIT 1")
	Single<Vehicle> find(long id);

	@Transaction
	@Query("SELECT * FROM vehicles WHERE id = :id LIMIT 1")
	Single<VehicleInventory> getInventory(long id);
}

