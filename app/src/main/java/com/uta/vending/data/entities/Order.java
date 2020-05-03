package com.uta.vending.data.entities;

import android.os.*;

import androidx.annotation.*;
import androidx.room.*;

import com.uta.vending.data.converters.*;

import java.math.*;
import java.time.*;
import java.util.*;

@SuppressWarnings("WeakerAccess")
@Entity(
	tableName = "orders",
	foreignKeys =
		{
			@ForeignKey(entity = User.class,
				parentColumns = "id",
				childColumns = "user_id",
				onDelete = ForeignKey.CASCADE),
			@ForeignKey(entity = User.class,
				parentColumns = "id",
				childColumns = "operator_id",
				onDelete = ForeignKey.CASCADE),
			@ForeignKey(entity = Vehicle.class,
				parentColumns = "id",
				childColumns = "vehicle_id",
				onDelete = ForeignKey.CASCADE)
		},
	indices = {
		@Index(name = "order_user_id_index", value = {"user_id"}),
		@Index(name = "order_operator_id_index", value = {"operator_id"}),
		@Index(name = "order_vehicle_id_index", value = {"vehicle_id"})
	}
)
public class Order
{
	@PrimaryKey(autoGenerate = true)
	public long id;

	@ColumnInfo(name = "user_id")
	public long userId;

	@ColumnInfo(name = "operator_id")
	public long operatorId;

	@TypeConverters(TimestampConverter.class)
	public LocalDateTime date;

	@TypeConverters(CurrencyConverter.class)
	public BigDecimal cost;

	@ColumnInfo(name = "vehicle_id")
	public long vehicleId;

	public String location;

	@ColumnInfo(name = "is_served")
	public boolean isServed;

	@TypeConverters(ListConverter.class)
	public List<OrderItem> items;

	public Order()
	{
	}

	@Ignore
	public Order(long userId, long operatorId, LocalDateTime date, long vehicleId, String location)
	{
		this.userId = userId;
		this.operatorId = operatorId;
		this.date = date;
		this.vehicleId = vehicleId;
		this.location = location;
		this.items = new ArrayList<>();
		this.cost = BigDecimal.ZERO;
	}

	public void addItem(OrderItem item)
	{
		items.add(item);
		computeCost();
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public void removeItem(OrderItem item)
	{
		items.removeIf(x -> x.item.id == item.item.id);
		computeCost();
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public void editItem(OrderItem item)
	{
		removeItem(item);
		addItem(item);
	}

	public BigDecimal computeCost()
	{
		cost = BigDecimal.ZERO;
		for (OrderItem item : items)
			cost = cost.add(item.getTotalCost());
		return cost;
	}
}

