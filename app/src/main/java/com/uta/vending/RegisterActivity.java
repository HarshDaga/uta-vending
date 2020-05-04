package com.uta.vending;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.*;

import com.uta.vending.data.*;
import com.uta.vending.data.entities.*;

import java.util.regex.*;

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


        User user = new User();
        user.firstName = firstNameText.getText().toString().trim();
        user.lastName = lastNameText.getText().toString().trim();
        user.email = emailText.getText().toString().trim();
        user.password = User.hash(passwordText.getText().toString().trim());
        user.phone = phoneText.getText().toString().trim();
        user.address = new Address();
        user.address.street = streetText.getText().toString().trim();
        user.address.city = cityText.getText().toString().trim();
        user.address.state = stateText.getText().toString().trim();
        user.address.zip = zipText.getText().toString().trim();
        user.role = Role.valueOf(roleButton.getText().toString().toUpperCase());

        if (isInvalid(user))
            return;

        appDb.userDao()
            .insert(user)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onRegistered);
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
        if (user.email.isEmpty() || !isValidEmailId(user.email))
        {
            makeShortToast("Enter a valid Email ID.");
            return true;
        }
        if (user.phone.length() != 10 || !TextUtils.isDigitsOnly(user.phone))
        {
            makeShortToast("Enter a valid Phone.");
            return true;
        }
        if (user.password.isEmpty())
        {
            makeShortToast("Enter a Password.");
            return true;
        }

        return false;
    }

    private void makeShortToast(String message)
    {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void onRegistered()
    {
        Toast.makeText(RegisterActivity.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
        Intent moveToLogin = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(moveToLogin);
        finish();
    }

    private boolean isValidEmailId(String email)
    {
        return Pattern
            .compile("^[\\w.]+@mavs\\.uta\\.edu")
            .matcher(email)
            .matches();
    }
}
