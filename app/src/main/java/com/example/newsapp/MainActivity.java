package com.example.newsapp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText username, password, repassword;
    Button signup, signin,skip;
    public static DBHelper DB;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        repassword= (EditText) findViewById(R.id.repass);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        skip= (Button) findViewById(R.id.btnskip);
        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                if (user.equals("") || pass.equals("") || repass.equals(""))
                {
                    Toast.makeText(MainActivity.this, "You have left it blank" ,Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(pass.equals(repass))
                    {
                        Boolean checkuser = DB.checkUsername(user);
                        if(checkuser ==false)
                        {
                            Boolean insert = DB.insertData(user,pass);
                            if(insert)
                            {
                                Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(MainActivity.this, "Registration Failed. DB Error ", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(MainActivity.this, "User already exists.", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Passwords not matching", Toast.LENGTH_LONG).show();
                }

            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if(user.equals("") || pass.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Boolean checkpass = DB.checkusernamepassword(user,pass);
                    if(checkpass== true)
                    {
                        Toast.makeText(MainActivity.this, "Sign in Successful", Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(intent2);

                    }
                    else {
                        Toast.makeText(MainActivity.this, "Credentials incorret", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        });


    }
}
