package com.uta.vending;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.Address;
import com.uta.vending.data.entities.Role;
import com.uta.vending.data.entities.User;

import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
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
    private void onClickRegister(View v) {
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

        // TODO: Add validation logic

        if (user.firstName.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Enter a First Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.lastName.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Enter a Last Name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.email.isEmpty() || !isValidEmailId(user.email)) {
            Toast.makeText(RegisterActivity.this, "Enter a valid Email ID.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.phone.isEmpty() || !isValidPhoneNumber(user.phone)) {
            Toast.makeText(RegisterActivity.this, "Enter a valid Phone.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Enter a Password.", Toast.LENGTH_SHORT).show();
            return;
        }

        appDb.userDao()
                .insert(user)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onRegistered);
    }

    private void onRegistered() {
        Toast.makeText(RegisterActivity.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
        Intent moveToLogin = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(moveToLogin);
        finish();
    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone.length() != 10)
            return false;
        return TextUtils.isDigitsOnly(phone);
    }
}
