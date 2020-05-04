package com.uta.vending.data.dao;

import androidx.room.*;

import com.uta.vending.data.converters.*;
import com.uta.vending.data.entities.*;

import java.time.*;
import java.util.*;

import io.reactivex.*;

@Dao
public interface RevenueDao
{
	@Insert
	Completable insert(Revenue... revenue);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	Completable update(Revenue... revenue);

	@Delete
	Completable delete(Revenue... revenue);

	@Query("SELECT * FROM revenue")
	Flowable<List<Order>> getAll();

	@TypeConverters(TimestampConverter.class)
	@Query("SELECT * FROM revenue WHERE date = :date")
	Flowable<List<Order>> getForDate(LocalDateTime date);
}
