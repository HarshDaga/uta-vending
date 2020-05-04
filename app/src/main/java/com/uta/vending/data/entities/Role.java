package com.uta.vending.data.entities;

@SuppressWarnings("unused")
public enum Role
{
	USER(0),
	OPERATOR(1),
	MANAGER(2);

	private int code;

	Role(int code)
	{
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}

}
