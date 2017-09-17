package com.example.rohit.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView mSignUp;
    private TextView mResetPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResetPass = (TextView) findViewById(R.id.main_resetpass_textview);

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
    }
}
