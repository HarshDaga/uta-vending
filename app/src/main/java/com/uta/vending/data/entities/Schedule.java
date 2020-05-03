package com.uta.vending.data.entities;

import androidx.room.*;

import com.uta.vending.data.converters.*;

import java.time.*;

@Entity(
	tableName = "schedule",
	foreignKeys =
		{
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
		@Index(name = "schedule_operator_id_index", value = {"operator_id"}),
		@Index(name = "schedule_vehicle_id_index", value = {"vehicle_id"})
	}
)
public class Schedule
{
	@PrimaryKey(autoGenerate = true)
	public long id;
	@ColumnInfo(name = "operator_id")
	public long operatorId;
	@ColumnInfo(name = "vehicle_id")
	public long vehicleId;
	public String location;
	@TypeConverters(TimestampConverter.class)
	public LocalDateTime start;
	@TypeConverters(TimestampConverter.class)
	public LocalDateTime end;

	public Schedule()
	{
	}

	@Ignore
	public Schedule(long operatorId, long vehicleId, String location, LocalDateTime start, LocalDateTime end)
	{
		this.operatorId = operatorId;
		this.vehicleId = vehicleId;
		this.location = location;
		this.start = start;
		this.end = end;
	}
}
