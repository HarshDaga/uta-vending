package com.uta.vending.data.entities;

import androidx.room.*;

@SuppressWarnings("unused")
@Entity(tableName = "vehicles")
public class Vehicle
{
	@PrimaryKey(autoGenerate = true)
	public long id;

	public String name;
	public String type;

	@Ignore
	public Vehicle(String name, String type)
	{
		this.name = name;
		this.type = type;
	}

	public Vehicle()
	{
	}
}

