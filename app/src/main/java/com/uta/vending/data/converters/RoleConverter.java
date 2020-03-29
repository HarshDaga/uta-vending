package com.uta.vending.data.converters;

import androidx.room.*;

import com.uta.vending.data.entities.*;


import static com.uta.vending.data.entities.Role.*;

@SuppressWarnings("unused")
public class RoleConverter
{
	@TypeConverter
	public static Role toRole(int role)
	{
		if (role == USER.getCode())
			return USER;
		else if (role == OPERATOR.getCode())
			return OPERATOR;
		else if (role == MANAGER.getCode())
			return MANAGER;
		else
			throw new IllegalArgumentException("Could not recognize role");
	}

	@TypeConverter
	public static Integer toInteger(Role role)
	{
		return role.getCode();
	}
}
