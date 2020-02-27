package com.uta.vending.data.dao;

import androidx.room.*;
import io.reactivex.*;

import com.uta.vending.data.entities.User;

@Dao
public interface UserDao
{
    @Insert
    Completable insert(User user);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password AND role = :role LIMIT 1")
    Single<User> find(String email, String password, String role);
}
