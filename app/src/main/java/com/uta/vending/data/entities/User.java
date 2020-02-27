package com.uta.vending.data.entities;

import androidx.room.*;

@SuppressWarnings("unused")
@Entity(tableName = "users")
public class User
{
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "first_name")
    public String firstName;
    @ColumnInfo(name = "last_name")
    public String lastName;
    public String email;
    public String password;
    public String phone;

    // TODO: Create Address class?
    public String street;
    public String city;
    public String state;
    @ColumnInfo(name = "zip_code")
    public String zip;

    // TODO: Create Role enum
    public String role;
}

