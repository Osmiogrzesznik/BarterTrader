package com.groupassignment2019.bartertraderappgithuballsetup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.groupassignment2019.bartertraderappgithuballsetup.ReusableListeners.ShowPasswordOnTouchListener;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button btnLogin;
    private TextView signUp;
    private ProgressBar progressBar_on_login_screen;
    private FirebaseAuth mAuth;
    private ImageView showPasswordIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();


        //mDialog=new ProgressDialog(this);
        progressBar_on_login_screen = findViewById(R.id.progressBar_on_login_screen);
        progressBar_on_login_screen.setVisibility(View.GONE);
        email=findViewById(R.id.email_login);
        pass=findViewById(R.id.password_login);
        btnLogin=findViewById(R.id.btn_login);
        signUp=findViewById(R.id.signup_txt);
        showPasswordIcon = findViewById(R.id.showLoginPasswordIcon);
        showPasswordIcon.setOnTouchListener(new ShowPasswordOnTouchListener(pass));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mEmail=email.getText().toString().trim();
                String mPass=pass.getText().toString().trim();


                if (TextUtils.isEmpty(mEmail)){
                    email.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(mPass)){
                    pass.setError("Required Field..");
                    return;
                }
//
//                mDialog.setMessage("Processing..");
//                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail,mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar_on_login_screen.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
//                            mDialog.dismiss();

                        }
                        else {

                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
  //                          mDialog.dismiss();
                        }
                        progressBar_on_login_screen.setVisibility(View.GONE);
                    }
                });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });




    }
}
