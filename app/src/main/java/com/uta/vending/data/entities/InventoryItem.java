package com.uta.vending.data.entities;

import androidx.room.*;

@Entity(
	tableName = "inventory",
	foreignKeys =
		{
			@ForeignKey(entity = Vehicle.class,
				parentColumns = "id",
				childColumns = "vehicle_id",
				onDelete = ForeignKey.CASCADE),
			@ForeignKey(entity = FoodItem.class,
				parentColumns = "id",
				childColumns = "food_id",
				onDelete = ForeignKey.CASCADE)
		}
)
public class InventoryItem
{
	@PrimaryKey(autoGenerate = true)
	public long id;

	@ColumnInfo(name = "vehicle_id")
	public long vehicleId;

	@Embedded(prefix = "food_")
	public FoodItem item;
	public int quantity;

	public InventoryItem()
	{
	}

	@Ignore
	public InventoryItem(long vehicleId, FoodItem item, int quantity)
	{
		this.vehicleId = vehicleId;
		this.item = item;
		this.quantity = quantity;
	}
}
