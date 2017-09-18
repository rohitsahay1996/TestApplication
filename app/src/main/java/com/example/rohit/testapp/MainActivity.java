package com.example.rohit.testapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView mSignUp;
    private TextView mResetPass;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    //ProgressBar
    private ProgressDialog mLoginProgress;

    //Firebase Declaration
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogin = (Button) findViewById(R.id.main_login_btn);
        mEmail = (EditText) findViewById(R.id.main_email_text);
        mPassword = (EditText) findViewById(R.id.main_password_text);
        mResetPass = (TextView) findViewById(R.id.main_resetpass_textview);
        mLoginProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        mResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i0 = new Intent(MainActivity.this,ResetPass.class);
                startActivity(i0);
            }
        });

        mSignUp = (TextView) findViewById(R.id.main_signup_textview1);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(i1);
            }
        });

        //----------------------------- Login Activity

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    mLoginProgress.setMessage("Login In progress");
                    mLoginProgress.setTitle("Login");
                    mLoginProgress.setCanceledOnTouchOutside(true);
                    mLoginProgress.show();
                    login_user(email , password);

                }
                else{
                    if(TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                        Toast.makeText(MainActivity.this, "Email Field Cannot be empty!", Toast.LENGTH_SHORT).show();
                    }
                     else if(!TextUtils.isEmpty(email) &&  TextUtils.isEmpty(password)){
                        Toast.makeText(MainActivity.this, "Password Field Cannot be empty!", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
                    {Toast.makeText(MainActivity.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();}


                }

            }
        });





    }

    private void login_user(String email, String password) {

        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login Sucessfull!!", Toast.LENGTH_SHORT).show();

                    mLoginProgress.dismiss();
                    Intent loginIntent = new Intent(MainActivity.this,bodyActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                    finish();

                }
                else {

                    mLoginProgress.hide();
                    Toast.makeText(MainActivity.this, "Login fail..", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
