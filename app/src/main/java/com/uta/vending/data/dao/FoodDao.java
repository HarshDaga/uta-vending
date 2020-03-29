package com.uta.vending.data.dao;

import androidx.room.*;

import com.uta.vending.data.entities.*;

import java.util.*;

import io.reactivex.*;

@Dao
public interface FoodDao
{
	@Insert
	Completable insert(FoodItem... item);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	Completable update(FoodItem... item);

	@Query("SELECT * FROM food_items")
	Flowable<List<FoodItem>> getAll();

	@Query("SELECT * FROM food_items WHERE type = :type LIMIT 1")
	Single<FoodItem> find(String type);

	@Query("SELECT * FROM food_items WHERE id = :id LIMIT 1")
	Single<FoodItem> find(long id);
}