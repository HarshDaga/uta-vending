package com.uta.vending.data.entities;

import java.math.*;

public class OrderItem
{
	public FoodItem item;
	public int quantity;

	public OrderItem(FoodItem item, int quantity)
	{
		this.item = item;
		this.quantity = quantity;
	}

	@SuppressWarnings("WeakerAccess")
	public BigDecimal getTotalCost()
	{
		return new BigDecimal(quantity).multiply(item.cost);
	}
}
