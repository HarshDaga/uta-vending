package com.uta.vending.data.entities;

import androidx.room.*;

import com.uta.vending.data.converters.*;

import java.math.*;
import java.time.*;

@Entity(tableName = "revenue")
public class Revenue
{
	@PrimaryKey(autoGenerate = true)
	public long id;

	@TypeConverters(TimestampConverter.class)
	public LocalDateTime date;

	@TypeConverters(CurrencyConverter.class)
	public BigDecimal amount;
}
