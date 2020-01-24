package com.example.st_fashion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText confirm;
    Button CreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = (EditText) findViewById(R.id.editTextUsername1);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword1);
        confirm = (EditText) findViewById(R.id.editTextConfirm);
        CreateAccount = (Button) findViewById(R.id.btnCreate);
    }

    public void CreateAcoount(View view) {
        int result = -1;
        DatabaseHelper db = new DatabaseHelper(this);
        if(!password.getText().toString().equals(confirm.getText().toString()))
        {
            Toast.makeText(this,getApplication().getResources().getString(R.string.signup_passmatch),Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(username.getText().toString().length() > 20)
            {
                Toast.makeText(this,getApplication().getResources().getString(R.string.signup_maxlen),Toast.LENGTH_SHORT).show();
            }
            else if(!isEmailValid(email.getText().toString() ) )
            {
                Toast.makeText(this,getApplication().getResources().getString(R.string.signup_invalidemail),Toast.LENGTH_SHORT).show();
            }
            else if(password.getText().toString().length() < 5)
            {
                Toast.makeText(this,getApplication().getResources().getString(R.string.signup_shortpass),Toast.LENGTH_SHORT).show();
            }
            else
            {
                UsersDb user = new UsersDb(username.getText().toString(),email.getText().toString(),password.getText().toString());
                result = db.addUser(user);
                if(result == 0)
                {
                    Toast.makeText(this,getApplication().getResources().getString(R.string.signup_useralready),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,getApplication().getResources().getString(R.string.signup_succes),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    public static boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
