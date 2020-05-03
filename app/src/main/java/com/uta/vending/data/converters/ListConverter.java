package com.uta.vending.data.converters;

import androidx.room.*;

import com.uta.vending.data.entities.*;

import java.util.*;

public class ListConverter extends ConverterBase
{
	@TypeConverter
	public static List<OrderItem> stringToOrderItemList(String data)
	{
		return fromJson(data);
	}

	@TypeConverter
	public static String orderItemsToString(List<OrderItem> items)
	{
		return toJson(items);
	}
}
