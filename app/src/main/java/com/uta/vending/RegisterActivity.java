package com.uta.vending;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import io.reactivex.android.schedulers.*;
import io.reactivex.schedulers.*;

public class RegisterActivity extends AppCompatActivity
{
	AppDatabase appDb;
	EditText firstNameText;
	EditText lastNameText;
	EditText emailText;
	EditText passwordText;
	EditText phoneText;
	EditText streetText;
	EditText cityText;
	EditText stateText;
	EditText zipText;
	RadioGroup roleGroup;
	Button btnRegister;


	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		appDb = AppDatabase.getInstance(this);
		firstNameText = findViewById(R.id.FirstName);
		lastNameText = findViewById(R.id.LastName);
		emailText = findViewById(R.id.Email);
		passwordText = findViewById(R.id.Password);
		phoneText = findViewById(R.id.Phone);
		streetText = findViewById(R.id.Street);
		cityText = findViewById(R.id.City);
		stateText = findViewById(R.id.State);
		zipText = findViewById(R.id.Zip);
		roleGroup = findViewById(R.id.RadioGroupRole);
		btnRegister = findViewById(R.id.RegisterButton);

		btnRegister.setOnClickListener(this::onClickRegister);
	}

	@SuppressLint("CheckResult")
	private void onClickRegister(View v)
	{
		int buttonId = roleGroup.getCheckedRadioButtonId();
		RadioButton roleButton = findViewById(buttonId);
		String role = roleButton.getText().toString();

		User user = new User();
		user.firstName = firstNameText.getText().toString().trim();
		user.lastName = lastNameText.getText().toString().trim();
		user.email = emailText.getText().toString().trim();
		user.password = passwordText.getText().toString().trim();
		user.phone = phoneText.getText().toString().trim();
		user.address = new Address();
		user.address.street = streetText.getText().toString().trim();
		user.address.city = cityText.getText().toString().trim();
		user.address.state = stateText.getText().toString().trim();
		user.address.zip = zipText.getText().toString().trim();
		user.role = Role.valueOf(role.toUpperCase());

		// TODO: Add validation logic

		appDb.userDao()
			.insert(user)
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::onRegistered);
	}

	private void onRegistered()
	{
		Toast.makeText(RegisterActivity.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
		Intent moveToLogin = new Intent(RegisterActivity.this, MainActivity.class);
		startActivity(moveToLogin);
	}
}
