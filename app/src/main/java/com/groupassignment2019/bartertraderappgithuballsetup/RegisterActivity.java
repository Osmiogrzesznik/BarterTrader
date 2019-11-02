package com.groupassignment2019.bartertraderappgithuballsetup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText phone;
    private EditText pass;
    private Button btnReg;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth= FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);
        progressBar = findViewById(R.id.registrationPageProgressBar);
        firstname=findViewById(R.id.first_reg);
        lastname=findViewById(R.id.last_reg);
        email=findViewById(R.id.email_reg);
        phone=findViewById(R.id.phone_reg);
        pass=findViewById(R.id.password_reg);
        btnReg=findViewById(R.id.btn_reg);



        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();
                String mmsmsm = "fabian";

                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mPass)){
                    pass.setError("Required Field..");
                    return;
                }

                if (mPass.length() <6){
                    pass.setError("Password muust be >= 6 characters");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            Toast.makeText(getApplicationContext(),"Successfull",Toast.LENGTH_SHORT).show();
                            //mDialog.dismiss();
                            progressBar.setVisibility(View.INVISIBLE);

                        }else {
                            Toast.makeText(getApplicationContext(),"Failed..",Toast.LENGTH_SHORT).show();
                            //mDialog.dismiss();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });


    }


}
