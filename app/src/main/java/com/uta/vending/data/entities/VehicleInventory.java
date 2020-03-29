package com.uta.vending.data.entities;

import androidx.room.*;

import java.util.*;

@SuppressWarnings("unused")
public class VehicleInventory
{
	@Embedded
	public Vehicle vehicle;

	@Relation(
		entity = InventoryItem.class,
		parentColumn = "id",
		entityColumn = "vehicle_id"
	)
	public List<InventoryItem> inventory;
}
