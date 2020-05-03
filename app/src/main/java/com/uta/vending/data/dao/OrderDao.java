package com.uta.vending.data.dao;

import androidx.room.*;

import com.uta.vending.data.entities.*;

import java.util.*;

import io.reactivex.*;

@Dao
public interface OrderDao
{
	@Insert
	Completable insert(Order... orders);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	Completable update(Order... orders);

	@Delete
	Completable delete(Order... orders);

	@Query("SELECT * FROM orders WHERE user_id = :userId")
	Flowable<List<Order>> getAll(long userId);

	@Query("SELECT COUNT(*) FROM orders WHERE operator_id = :operatorId")
	Single<Integer> getTxCount(long operatorId);

	@Query("SELECT * FROM orders WHERE id = :orderId")
	Single<Order> findOrder(long orderId);
}

