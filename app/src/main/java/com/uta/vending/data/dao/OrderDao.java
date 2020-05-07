package com.uta.vending.data.dao;

import androidx.room.*;

import com.uta.vending.data.entities.*;

import java.util.*;

import io.reactivex.*;

@Dao
public interface OrderDao
{
	@Insert
	Single<Long> insert(Order order);

	@Insert
	Single<long[]> insert(Order... orders);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	Completable update(Order... orders);

	@Delete
	Completable delete(Order... orders);

	@Query("SELECT * FROM orders WHERE user_id = :userId")
	Flowable<List<Order>> getAll(long userId);

	@Query("SELECT COUNT(*) FROM orders WHERE operator_id = :operatorId LIMIT 1")
	Single<Integer> getTxCount(long operatorId);

	@Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
	Single<Order> findOrder(long orderId);
}

