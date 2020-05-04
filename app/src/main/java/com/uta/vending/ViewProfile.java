package com.uta.vending;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class ViewProfile extends AppCompatActivity
{
	AppDatabase appDb;
	long id;
	User user;

	TextView txtPassword;
	EditText editText22;
	EditText editText21;
	EditText editText20;
	EditText editText18;
	EditText editText14;
	EditText editText13;
	EditText editText8;
	TextView txtEmail;
	TextView txtRole;

	Button btnUpdateProfile;
	Button btnDeleteProfile;

	private long getIdFromIntent()
	{
		return this.getIntent() == null
			? 0
			: this.getIntent().getLongExtra("ID", 0);
	}

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_profile);

		txtPassword = findViewById(R.id.editText4);
		editText22 = findViewById(R.id.editText22);
		editText21 = findViewById(R.id.editText21);
		editText20 = findViewById(R.id.editText20);
		editText18 = findViewById(R.id.editText18);
		editText14 = findViewById(R.id.editText14);
		editText13 = findViewById(R.id.editText13);
		editText8 = findViewById(R.id.editText8);
		txtEmail = findViewById(R.id.editText17);
		txtRole = findViewById(R.id.textView11);

		appDb = AppDatabase.getInstance(this);
		id = getIdFromIntent();
		appDb.userDao().getUser(id).subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onUserFound, this::onUserLookupError);

		btnDeleteProfile = findViewById(R.id.DeleteProfile);
		btnDeleteProfile.setOnClickListener(this::onClickDelete);

		btnUpdateProfile = findViewById(R.id.UpdateProfile);
		btnUpdateProfile.setOnClickListener(this::onClickUpdate);
	}

	@SuppressLint("CheckResult")
	public void onClickUpdate(View v)
	{
		user.firstName = editText8.getText().toString();
		user.lastName = editText13.getText().toString();
		user.phone = editText14.getText().toString();
		if (user.address == null)
			user.address = new Address();
		user.address.street = editText18.getText().toString();
		user.address.city = editText20.getText().toString();
		user.address.state = editText22.getText().toString();
		user.address.zip = editText21.getText().toString();

		if (isInvalid(user))
			return;

		appDb.userDao().update(user).subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onUpdateComplete);
	}

	private void onUpdateComplete()
	{
		Toast.makeText(ViewProfile.this, "Update successful.", Toast.LENGTH_LONG).show();
	}

	private void onUserLookupError(Throwable throwable)
	{
		Log.e("ViewProfile", "User lookup error id:" + id, throwable);
		Toast.makeText(ViewProfile.this, "Invalid User", Toast.LENGTH_LONG).show();
	}

	private void onUserFound(User user)
	{
		this.user = user;
		this.editText8.setText(this.user.firstName);
		this.editText13.setText(this.user.lastName);
		this.txtPassword.setText(this.user.password);
		this.txtEmail.setText(this.user.email);
		this.editText14.setText(this.user.phone);
		this.editText18.setText(this.user.address != null ? this.user.address.street : "");
		this.editText20.setText(this.user.address != null ? this.user.address.city : "");
		this.editText22.setText(this.user.address != null ? this.user.address.state : "");
		this.editText21.setText(this.user.address != null ? this.user.address.zip : "");
		this.txtRole.setText(this.user.role.name());
	}

	private void onClickDelete(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfile.this);
		builder.setTitle("Delete Profile");
		builder.setMessage("You are about to delete your profile. Are you sure?");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", this::onDeleteConfirmed);
		builder.setNegativeButton("No", null);

		builder.show();
	}

	@SuppressLint("CheckResult")
	private void onDeleteConfirmed(DialogInterface dialog, int which)
	{
		appDb.userDao().delete(user)
			.subscribe(this::onDeleteCompleted);
	}

	private void onDeleteCompleted()
	{
		Toast.makeText(getApplicationContext(),
			"Account deleted.",
			Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(ViewProfile.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private boolean isInvalid(User user)
	{
		if (user.firstName.isEmpty())
		{
			makeShortToast("Enter a First Name.");
			return true;
		}
		if (user.lastName.isEmpty())
		{
			makeShortToast("Enter a Last Name.");
			return true;
		}
		if (user.phone.length() != 10 || !TextUtils.isDigitsOnly(user.phone))
		{
			makeShortToast("Enter a valid Phone.");
			return true;
		}

		return false;
	}

	private void makeShortToast(String message)
	{
		Toast.makeText(ViewProfile.this, message, Toast.LENGTH_SHORT).show();
	}
}
