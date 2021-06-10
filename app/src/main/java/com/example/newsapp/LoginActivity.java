package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText username1, password1;
    Button signin1;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username1 =(EditText) findViewById(R.id.username1);
        password1 = (EditText) findViewById(R.id.password1);
        signin1 = (Button) findViewById(R.id.btnsignin1);
        DB = new DBHelper((View.OnClickListener) this);



        signin1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                String user = username1.getText().toString();
                String pass = password1.getText().toString();
                if(user.equals("") || pass.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Boolean checkpass = DB.checkusernamepassword(user,pass);
                    if(checkpass== true)
                    {
                        Toast.makeText(LoginActivity.this, "Sign in Successful", Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent2);

                    }
                    else
                        Toast.makeText(LoginActivity.this, "Credentials incorret", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

}