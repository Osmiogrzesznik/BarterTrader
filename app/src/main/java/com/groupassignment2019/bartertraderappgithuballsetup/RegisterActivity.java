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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.groupassignment2019.bartertraderappgithuballsetup.Helpers.InputValidator;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.ShowPasswordOnTouchListener;
import com.groupassignment2019.bartertraderappgithuballsetup.models.UserDataModel;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_firstname;
    private EditText et_lastname;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_pass;
    private DatabaseReference DB_users;
    private FirebaseAuth mAuth;
    private ProgressBar progressCircle;
    private Random rnd;
    private InputValidator inputValidator;
    private boolean isUserCreationInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        inputValidator = new InputValidator(this);
        mAuth = FirebaseAuth.getInstance();
        DB_users = FirebaseDatabase.getInstance().getReference("users");
        progressCircle = findViewById(R.id.registrationPageProgressBar);
        et_firstname = findViewById(R.id.first_reg);
        et_lastname = findViewById(R.id.last_reg);
        et_email = findViewById(R.id.email_reg);
        et_phone = findViewById(R.id.phone_reg);
        et_pass = findViewById(R.id.password_reg);
        Button btn_Reg = findViewById(R.id.btn_reg);
        ImageView showRegisterPasswordIcon = findViewById(R.id.showRegisterPasswordIcon);
        showRegisterPasswordIcon.setOnTouchListener(new ShowPasswordOnTouchListener(et_pass));

        progressCircle.setVisibility(View.INVISIBLE);
        isUserCreationInProgress = false;


        //TEST
//        rnd = new Random();
//        if (rnd == null) throw new NullPointerException("random is still null");
//
//        int randSuffix = rnd.nextInt() & Integer.MAX_VALUE; // zero out the sign bit;
//        et_firstname.setText("Testeusz" + randSuffix);
//        et_lastname.setText("Testowski" + randSuffix);
//        et_email.setText("test" + randSuffix + "@test.com");
//        et_phone.setText("0123456789" + randSuffix);
//        et_pass.setText("password");

        btn_Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String firstname = et_firstname.getText().toString().trim();
                String lastname = et_lastname.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                EditText[] allFields = {et_firstname, et_lastname, et_email, et_pass, et_phone};

                if (
                        inputValidator.isValidEmail(et_email)
                                && inputValidator.isValidPhone(et_phone)
                                && inputValidator.notEmptyAll(allFields)
                                && inputValidator.notShorterThan(et_pass, 6)
                ) {
                    registerUserAndCreateDatabaseEntry();
                } else {
                }


            }
        });


    }

    private void registerUserAndCreateDatabaseEntry() {
        String email = et_email.getText().toString().trim();
        String password = et_pass.getText().toString().trim();
        progressCircle.setVisibility(View.INVISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            CreateUserInDatabase(uid);
                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void CreateUserInDatabase(String uid) {
        String firstname = et_firstname.getText().toString().trim();
        String lastname = et_lastname.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        final UserDataModel newUser = new UserDataModel(firstname, lastname, phone, uid);
        DB_users.child(uid).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserWasAdded_NowShowNextActivity(newUser);
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void UserWasAdded_NowShowNextActivity(UserDataModel newUser) {
        progressCircle.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Welcome " + newUser.getFullName() + " ! ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }


}
