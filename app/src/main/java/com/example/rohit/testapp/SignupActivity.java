package com.example.rohit.testapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Date;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private Button mSignUp;
    private TextView mAlready;
    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private ProgressDialog signup_dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSignUp = (Button) findViewById(R.id.signup_btn);
        mAlready = (TextView) findViewById(R.id.signup_already);
        mEmail = (EditText) findViewById(R.id.signup_email);
        mDisplayName = (EditText) findViewById(R.id.signup_display_name);
        mPassword = (EditText) findViewById(R.id.signup_password);
        //Progress Dialog
        signup_dialog = new ProgressDialog(this);
        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Signup Already

        mAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(SignupActivity.this,MainActivity.class);
                startActivity(i3);
            }
        });

        //SignnUp Button
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String display_name = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();


                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(display_name)){
                    signup_dialog.setMessage("Sign up progress");
                    signup_dialog.setTitle("Signup");
                    signup_dialog.setCanceledOnTouchOutside(true);
                    signup_dialog.show();
                    signup_user(display_name , email , password);

                }
                else{
                    if(TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(display_name)){
                        Toast.makeText(SignupActivity.this, "Email Field Cannot be empty!", Toast.LENGTH_SHORT).show();
                    }
                    else if(!TextUtils.isEmpty(email) &&  TextUtils.isEmpty(password) && !TextUtils.isEmpty(display_name)){
                        Toast.makeText(SignupActivity.this, "Password Field Cannot be empty!", Toast.LENGTH_SHORT).show();
                    }
                    else if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && TextUtils.isEmpty(display_name))
                    {Toast.makeText(SignupActivity.this, "Display Fields cannot be empty!", Toast.LENGTH_SHORT).show();}

                    else if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty(display_name))
                    {Toast.makeText(SignupActivity.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();}


                }


            }
        });


    }

    private void signup_user(final String display_name , final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               signup_dialog.dismiss();

               FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
               String uid = current_user.getUid();

               //DeviceToken

               String device_token = FirebaseInstanceId.getInstance().getToken();

               //Firebase Database

               mDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
               final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

               HashMap<String , String> user_map = new HashMap<String, String>();
               user_map.put("Display name" , display_name);
               user_map.put("Email" , email);
               user_map.put("Password" , password);
               user_map.put("Device token" , device_token);
               user_map.put("Date",currentDate);


               mDataBase.setValue(user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){

                           Intent signup_intent = new Intent(SignupActivity.this,bodyActivity.class);
                           startActivity(signup_intent);
                           signup_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           finish();
                       }
                       else{

                           Toast.makeText(SignupActivity.this, "SignUp Failed!!", Toast.LENGTH_SHORT).show();
                       }

                   }
               });



           }
                else{
               Toast.makeText(SignupActivity.this, "SignUp Failed!!", Toast.LENGTH_SHORT).show();
               signup_dialog.hide();


           }

            }
        });



    }
}
