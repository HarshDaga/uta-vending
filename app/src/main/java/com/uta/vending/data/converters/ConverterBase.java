package com.uta.vending.data.converters;

import com.google.gson.*;
import com.google.gson.reflect.*;

import java.lang.reflect.*;

abstract class ConverterBase
{
	static <T> String toJson(T value)
	{
		Gson gson = new Gson();
		return gson.toJson(value);
	}

	static <T> T fromJson(String json)
	{
		Gson gson = new Gson();

		Type type = new TypeToken<T>()
		{
		}.getType();

		return gson.fromJson(json, type);
	}

	static <T> T[] fromJson(String json, Class<T[]> klass)
	{
		Gson gson = new Gson();

		Type type = new TypeToken<T>()
		{
		}.getType();

		return gson.fromJson(json, klass);
	}
}