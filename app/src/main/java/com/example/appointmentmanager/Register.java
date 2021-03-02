package com.example.appointmentmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Register extends AppCompatActivity implements AsyncCallback<BackendlessUser> {

    EditText userName, Mail, Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.editTextTextPersonName);
        Mail = findViewById(R.id.editTextTextPersonMail);
        Password = findViewById(R.id.editTextTextPassword);
    }

    public void register(View view) {

        BackendlessUser user = new BackendlessUser();
        user.setEmail(Mail.getText().toString());
        user.setPassword(Password.getText().toString());
        user.setProperty("UserName", userName.getText().toString());
        Backendless.UserService.register(user, this);


    }

    @Override
    public void handleResponse(BackendlessUser response) {
        Toast.makeText(this, "Registration completed successfully", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void handleFault(BackendlessFault fault) {
        if (fault.getCode().equals("3033")) {
            Toast.makeText(this, "User exists", Toast.LENGTH_SHORT).show();

        } else Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();

    }
}