package com.uta.vending.data.converters;

import androidx.room.*;

import java.math.*;

public class CurrencyConverter
{
	@TypeConverter
	public String bigDecimalToString(BigDecimal input)
	{
		return input.toPlainString();
	}

	@TypeConverter
	public BigDecimal stringToBigDecimal(String input)
	{
		return new BigDecimal(input);
	}

}
