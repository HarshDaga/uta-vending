package com.uta.vending;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
{
    EditText emailText;
    EditText passwordText;
    Button btnLogin;
    Button btnRegister;
    AppDatabase appDb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDb = AppDatabase.getDatabase(this);
        emailText = findViewById(R.id.LoginEmail);
        passwordText = findViewById(R.id.LoginPassword);
        btnLogin = findViewById(R.id.LoginButton);
        btnRegister = findViewById(R.id.RegisterNowButton);

        btnRegister.setOnClickListener(this::onClickRegister);
        btnLogin.setOnClickListener(this::onClickLogin);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @SuppressLint("CheckResult")
    private void onClickLogin(View v)
    {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        appDb.userDao()
                .find(email, password, "User")
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUserFound, this::onUserLookupError);
    }

    private void onClickRegister(View v)
    {
        Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void onUserFound(@NonNull User user)
    {
        Toast.makeText(MainActivity.this, String.format("Welcome %s", user.firstName), Toast.LENGTH_LONG).show();
        Intent afterLogin = new Intent(MainActivity.this, AfterLoginActivity.class);
        startActivity(afterLogin);
    }

    private void onUserLookupError(Throwable throwable)
    {
        Log.e("MainActivity", "User lookup error", throwable);
        Toast.makeText(MainActivity.this, "Invalid User", Toast.LENGTH_LONG).show();
    }
}
