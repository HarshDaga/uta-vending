package com.uta.vending.data.entities;

import androidx.room.*;

import com.uta.vending.data.converters.*;

import java.math.*;

@SuppressWarnings("unused")
@Entity(tableName = "food_items")
public class FoodItem
{
	@PrimaryKey(autoGenerate = true)
	public long id;

	public String type;

	@TypeConverters(CurrencyConverter.class)
	public BigDecimal cost;

	public FoodItem()
	{
	}

	@Ignore
	public FoodItem(String type, BigDecimal cost)
	{
		this.type = type;
		this.cost = cost;
	}

	@Ignore
	public FoodItem(String type, double cost)
	{
		this.type = type;
		this.cost = new BigDecimal(cost);
	}
}