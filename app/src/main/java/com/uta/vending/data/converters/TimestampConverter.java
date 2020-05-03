package com.uta.vending.data.converters;

import android.os.*;

import androidx.annotation.*;
import androidx.room.*;

import java.time.*;

public class TimestampConverter
{
	@RequiresApi(api = Build.VERSION_CODES.O)
	@TypeConverter
	public LocalDateTime fromTimestamp(Long value)
	{
		return value == null
			? null
			: LocalDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.systemDefault());
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@TypeConverter
	public Long toTimestamp(LocalDateTime date)
	{
		if (date == null)
		{
			return null;
		}
		else
		{
			return date.atZone(ZoneId.systemDefault()).toEpochSecond();
		}
	}
}

