package com.uta.vending.data.entities;

import androidx.room.*;

import com.uta.vending.data.converters.*;

import org.mindrot.jbcrypt.*;

@SuppressWarnings("unused")
@Entity(tableName = "users")
public class User
{
	@PrimaryKey(autoGenerate = true)
	public long id;

	@ColumnInfo(name = "first_name")
	public String firstName;
	@ColumnInfo(name = "last_name")
	public String lastName;
	public String email;
	public String password;
	public String phone;

	@Embedded
	public Address address;

	@TypeConverters(RoleConverter.class)
	public Role role;

	public User()
	{
	}

	@Ignore
	public User(String firstName, String lastName, String email, String password, String phone, Address address, Role role)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.address = address;
		this.role = role;
	}

	public static String hash(String password)
	{
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}

	public boolean checkPassword(String password)
	{
		return BCrypt.checkpw(password, this.password);
	}
}

