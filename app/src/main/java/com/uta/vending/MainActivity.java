package com.uta.vending;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.Role;
import com.uta.vending.data.entities.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    EditText emailText;
    EditText passwordText;
    Button btnLogin;
    Button btnRegister;
    RadioGroup roleGroup;
    AppDatabase appDb;
    RadioGroup loginRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDb = AppDatabase.getInstance(this);
        emailText = findViewById(R.id.LoginEmail);
        passwordText = findViewById(R.id.LoginPassword);
        btnLogin = findViewById(R.id.LoginButton);
        btnRegister = findViewById(R.id.RegisterNowButton);
        roleGroup = findViewById(R.id.LoginRole);
        btnRegister.setOnClickListener(this::onClickRegister);
        btnLogin.setOnClickListener(this::onClickLogin);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("CheckResult")
    private void onClickLogin(View v) {
        int buttonId = roleGroup.getCheckedRadioButtonId();
        RadioButton roleButton = findViewById(buttonId);
        Role role = Role.valueOf(roleButton.getText().toString().toUpperCase());

        String email = emailText.getText().toString().trim();

        appDb.userDao()
                .getUser(email, role)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUserFound, this::onUserLookupError);
    }

    private void onClickRegister(View v) {
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void onUserFound(@NonNull User user) {
        String password = passwordText.getText().toString().trim();
        if (!user.checkPassword(password)) {
            Log.d(MainActivity.class.getName(), "Invalid password");
            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = null;
        switch (user.role) {
            case MANAGER:
                intent = new Intent(MainActivity.this, ManagerHomeScreen.class);
                break;
            case OPERATOR:
                intent = new Intent(MainActivity.this, OperatorHomeScreen.class);
                break;
            case USER:
                intent = new Intent(MainActivity.this, UserHomeScreen.class);

                break;
        }
        Toast.makeText(MainActivity.this, String.format("Welcome %s", user.firstName), Toast.LENGTH_LONG).show();
        intent.putExtra("ID", user.id);
        startActivity(intent);
    }

    private void onUserLookupError(Throwable throwable) {
        Log.e("MainActivity", "User lookup error", throwable);
        Toast.makeText(MainActivity.this, "No such user found", Toast.LENGTH_LONG).show();
    }
}
