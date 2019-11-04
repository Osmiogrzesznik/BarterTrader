package com.groupassignment2019.bartertraderappgithuballsetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.InputValidator;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.ShowPasswordOnTouchListener;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_firstname;
    private EditText et_lastname;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_pass;
    private Button btn_Reg;
    private ImageView showRegisterPasswordIcon;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Random rnd;
    private InputValidator inputValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inputValidator = new InputValidator();
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.registrationPageProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        et_firstname = findViewById(R.id.first_reg);
        et_lastname = findViewById(R.id.last_reg);
        et_email = findViewById(R.id.email_reg);
        et_phone = findViewById(R.id.phone_reg);
        et_pass = findViewById(R.id.password_reg);
        btn_Reg = findViewById(R.id.btn_reg);
        showRegisterPasswordIcon = findViewById(R.id.showRegisterPasswordIcon);
        showRegisterPasswordIcon.setOnTouchListener(new ShowPasswordOnTouchListener(et_pass));

        //TEST
        rnd = new Random();
        if (rnd == null) throw new NullPointerException("random is still null");


        btn_Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = et_email.getText().toString().trim();
                String password = et_pass.getText().toString().trim();
                String firstname = et_firstname.getText().toString().trim();
                String lastname = et_lastname.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                EditText[] allFields = {et_firstname,et_lastname,et_email,et_pass,et_phone};

                if(
                        inputValidator.notEmptyAll(allFields)
                        && inputValidator.notShorterThan(et_pass,6)
                        && inputValidator.isValidEmail(et_email)
                        && inputValidator.isValidPhone(et_phone)
                ){
                    registerUserAndCreateDatabaseEntry(et_firstname,et_lastname,et_email,et_pass,et_phone);
                }
                else {
                    return;
                }




            }
        });


    }

    private void registerUserAndCreateDatabaseEntry(EditText et_firstname, EditText et_lastname, EditText et_email, EditText et_pass, EditText et_phone) {

    }


}
