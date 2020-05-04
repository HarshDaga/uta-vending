package com.uta.vending.data.entities;

import androidx.room.*;

import com.uta.vending.data.converters.*;

import java.time.*;

public class Schedule
{
	@ColumnInfo(name = "op_id")
	public long operatorId;
	@ColumnInfo(name = "loc")
	public String location;
	@TypeConverters(TimestampConverter.class)
	public LocalDateTime start;
	@TypeConverters(TimestampConverter.class)
	public LocalDateTime end;
}
