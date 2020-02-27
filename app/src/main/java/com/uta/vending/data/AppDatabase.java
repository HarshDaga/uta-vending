package com.uta.vending.data;

import android.content.*;
import android.util.*;

import androidx.annotation.*;
import androidx.room.*;
import androidx.room.RoomDatabase;

import com.uta.vending.data.entities.*;
import com.uta.vending.data.dao.*;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    private static final String DB_NAME = "data.db";
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(@NonNull Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (AppDatabase.class)
            {
                Log.d("AppDatabase", "Creating Database instance");
                INSTANCE = Room
                        .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                        .build();
            }
        }

        return INSTANCE;
    }

    abstract public UserDao userDao();
}
