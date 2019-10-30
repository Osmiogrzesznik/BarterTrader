package com.groupassignment2019.bartertraderappgithuballsetup;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        et = findViewById(R.id.email_login);



    }

    public void loginuser(View view) {
        if (et.getText().toString().matches(Patterns.EMAIL_ADDRESS.toString())){
            et.setError("this is not valid email");
        }
        //edittext.setError shows user that the have a mistake

    }
}
