package com.uta.vending.data.entities;

import android.os.*;

import androidx.annotation.*;
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

	@RequiresApi(api = Build.VERSION_CODES.O)
	public boolean containsTime(LocalDateTime time)
	{
		if (start == null || end == null)
			return false;

		return time.isAfter(start) && time.isBefore(end);
	}
}
