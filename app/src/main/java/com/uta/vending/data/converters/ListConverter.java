
package com.uta.vending.data.converters;

import androidx.room.*;

import com.uta.vending.data.entities.*;

public class ListConverter extends ConverterBase
{
	@TypeConverter
	public static OrderItem[] stringToOrderItemList(String data)
	{
		return fromJson(data, OrderItem[].class);
	}

	@TypeConverter
	public static String orderItemsToString(OrderItem[] items)
	{
		return toJson(items);
	}
}