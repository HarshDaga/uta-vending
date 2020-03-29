package com.uta.vending;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

import org.jetbrains.annotations.*;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "data.db";
	private static final String TABLE_NAME = "Users";
	private static final String COL_ID = "ID";
	private static final String COL_FirstName = "FirstName";
	private static final String COL_LastName = "LastName";
	private static final String COL_Email = "Email";
	private static final String COL_Password = "Password";
	private static final String COL_Phone = "Phone";
	private static final String COL_Street = "Street";
	private static final String COL_City = "City";
	private static final String COL_State = "State";
	private static final String COL_Zip = "Zip";
	private static final String COL_Role = "Role";

	DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(@NotNull SQLiteDatabase db)
	{
		String createUsersTable = String.format(
			"CREATE TABLE %s (" +
				"%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT, " +
				"%s TEXT)",
			TABLE_NAME, COL_ID, COL_FirstName, COL_LastName, COL_Email, COL_Password, COL_Phone, COL_Street, COL_City, COL_State, COL_Zip, COL_Role
		);

		db.execSQL(createUsersTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
	{
		sqLiteDatabase.execSQL(String.format(" DROP TABLE IF EXISTS %s", TABLE_NAME));
		onCreate(sqLiteDatabase);
	}

	@SuppressWarnings("UnusedReturnValue")
	long addUser(String firstName, String lastName,
	             String email, String password,
	             String phone, String street, String city, String state, String zip,
	             String role)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(COL_FirstName, firstName);
		contentvalues.put(COL_LastName, lastName);
		contentvalues.put(COL_Email, email);
		contentvalues.put(COL_Password, password);
		contentvalues.put(COL_Phone, phone);
		contentvalues.put(COL_Street, street);
		contentvalues.put(COL_City, city);
		contentvalues.put(COL_State, state);
		contentvalues.put(COL_Zip, zip);
		contentvalues.put(COL_Role, role);
		long res = db.insert(TABLE_NAME, null, contentvalues);
		db.close();

		return res;
	}

	boolean checkIfUserExists(String email, String password)
	{
		String[] columns = {COL_ID};
		SQLiteDatabase db = getReadableDatabase();
		String selection = String.format("%s=? and %s=?", COL_Email, COL_Password);
		String[] selectionArgs = {email, password};
		Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();

		return count > 0;
	}
}
