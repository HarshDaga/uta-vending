package com.uta.vending.data.entities;

import androidx.room.*;

@SuppressWarnings("unused")
@Entity(tableName = "food_items")
public class FoodItem
{
	@PrimaryKey(autoGenerate = true)
	public long id;

	public String type;
	public int cost;

	public FoodItem()
	{
	}

	@Ignore
	public FoodItem(String type, int cost)
	{
		this.type = type;
		this.cost = cost;
	}
}