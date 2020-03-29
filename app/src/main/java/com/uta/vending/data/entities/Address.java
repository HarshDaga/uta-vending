package com.uta.vending.data.entities;

import androidx.room.*;

public class Address
{
	public String street;
	public String city;
	public String state;
	@ColumnInfo(name = "zip_code")
	public String zip;

	public Address()
	{
	}

	@Ignore
	public Address(String street, String city, String state, String zip)
	{
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
}
