package com.example.courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.Registration.FirstStepRegistration;

public class LoginOrRegistrationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_registration);

    }

    public void Login(View view) {
        Intent intent = new Intent(this, AuthorizationActivity.class);
        startActivity(intent);
    }

    public void Registration(View view) {
        Intent intent = new Intent(this, FirstStepRegistration.class);
        startActivity(intent);
    }
}
