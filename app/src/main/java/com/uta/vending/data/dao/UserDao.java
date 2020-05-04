package com.uta.vending.data.dao;

import androidx.room.*;

import com.uta.vending.data.converters.*;
import com.uta.vending.data.entities.*;

import java.util.*;

import io.reactivex.*;

@Dao
public interface UserDao
{
	@Insert
	Completable insert(User... users);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	Completable update(User... users);

	@Delete
	Completable delete(User... users);

	@TypeConverters(RoleConverter.class)
	@Query("SELECT * FROM users WHERE email = :email AND role = :role LIMIT 1")
	Single<User> getUser(String email, Role role);

	@Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
	Single<User> getUser(long userId);

	@TypeConverters(RoleConverter.class)
	@Query("SELECT * FROM users WHERE role = :role")
	Flowable<List<User>> getAll(Role role);
}

