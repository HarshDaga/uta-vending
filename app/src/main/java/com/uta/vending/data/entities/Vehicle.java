package com.uta.vending.data.entities;

import androidx.annotation.NonNull;
import androidx.room.*;

@SuppressWarnings("unused")
@Entity(tableName = "vehicles")
public class Vehicle
{
	@PrimaryKey(autoGenerate = true)
	public long id;

	public String name;
	public String type;

	@Embedded(prefix = "sch_cur_")
	public Schedule scheduleToday;
	@Embedded(prefix = "sch_nxt_")
	public Schedule scheduleNext;

	@Ignore
	public Vehicle(String name, String type)
	{
		this.name = name;
		this.type = type;

		scheduleToday = new Schedule();
		scheduleNext = new Schedule();
	}

	public Vehicle()
	{
		scheduleToday = new Schedule();
		scheduleNext = new Schedule();
	}

	@NonNull
	@Override
	public String toString() {
		return "Name:";
	}
}

