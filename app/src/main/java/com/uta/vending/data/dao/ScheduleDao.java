package com.uta.vending.data.dao;

import androidx.room.*;

import com.uta.vending.data.entities.*;

import java.util.*;

import io.reactivex.*;

@Dao
public interface ScheduleDao
{
	@Insert
	Completable insert(Schedule... schedules);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	Completable update(Schedule... schedules);

	@Delete
	Completable delete(Schedule... schedules);

	@Query("SELECT * FROM schedule")
	Flowable<List<Schedule>> getAll();

	@Query("SELECT * FROM schedule WHERE operator_id = :operatorId")
	Flowable<List<Schedule>> getAll(long operatorId);
}
