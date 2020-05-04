package com.uta.vending;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.uta.vending.data.AppDatabase;
import com.uta.vending.data.entities.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewProfile extends AppCompatActivity {

    Button btndeleteprofile;
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

    public static Intent getNavigationIntent(Context ctx, long id) {
        Intent lIntent = new Intent(ctx, ViewProfile.class);
        if (id != 0)
            lIntent.putExtra("ID", id);
        return lIntent;
    }


    public long getIdFromIntent(){

        if (this.getIntent() != null){
            return this.getIntent().getLongExtra("ID",0);
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        btnUpdateProfile = findViewById(R.id.UpdateProfile);

        appDb = AppDatabase.getInstance(this);
        id = getIdFromIntent();
        appDb.userDao().getUser(id).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUserFound, this::onUserLookupError);

        btndeleteprofile = findViewById(R.id.DeleteProfile);
        btndeleteprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(ViewProfile.this);
                builder.setTitle("Delete Profile");
                builder.setMessage("You are about to delete your profile. Do you really want to proceed ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You've choosen to delete all records", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.firstName = editText8.getText().toString();
                user.lastName = editText13.getText().toString();
                user.phone = editText14.getText().toString();
                user.address.street = editText18.getText().toString();
                user.address.city = editText20.getText().toString();
                user.address.state = editText22.getText().toString();
                user.address.zip = editText21.getText().toString();

                appDb.userDao().update(user).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onUpdateDone, this::onUpdateFailure);;
            }

            private void onUpdateDone() {
                Toast.makeText(ViewProfile.this, "Update successful.", Toast.LENGTH_LONG).show();
            }

            private void onUpdateFailure(Throwable throwable) {
                Log.e("ViewProfile", "Update failed.", throwable);
                Toast.makeText(ViewProfile.this, "Update failed.", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void onUserLookupError(Throwable throwable) {
        Log.e("ViewProfile", "User lookup error id:" + id, throwable);
        Toast.makeText(ViewProfile.this, "Invalid User", Toast.LENGTH_LONG).show();
    }

    private void onUserFound(User user) {
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
}
