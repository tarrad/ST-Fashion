package com.example.st_fashion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button changeLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        changeLang = (Button) findViewById(R.id.btnChangeLang);

        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showChangeLanguageDialog();
            }
        });

        //getHash();
        setNotification();
    }

    public void goToMenuActivity(View view) {
        String result = "0";
        DatabaseHelper db = new DatabaseHelper(this);
        String user_name = username.getText().toString();
        String pass = password.getText().toString();
        if(user_name.length() >= 5 && pass.length() >= 5)
        {
            UsersDb user = new UsersDb(user_name,null,pass);
            result = db.checkRegisteredUser(user);
            if(!result.equals("0"))
            {
                Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
                v.vibrate(2000);
                Intent intent = new Intent(this, Menu.class);
                intent.putExtra("username",user_name);
                intent.putExtra("email",result);
                resetDetails();
                startActivity(intent);
            }
            else {
                resetDetails();

                Toast.makeText(this,getApplication().getResources().getString(R.string.main_details),Toast.LENGTH_SHORT).show();
            }
        }
        else {
            resetDetails();
            Toast.makeText(this,getApplication().getResources().getString(R.string.main_length),Toast.LENGTH_SHORT).show();
        }
    }

    public void goToSignupActivity(View view) {
        Intent intent = new Intent(this,Signup.class);
        startActivity(intent);
    }

    public void resetDetails()
    {
        username.setText("");
        password.setText("");
    }

    public void getHash()
    {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.st_fashion",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }
        catch(PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

    public void setNotification()
    {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);

        Intent intent1 = new Intent(getApplicationContext(), MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                111, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void showChangeLanguageDialog()
    {
        final String[] listLanguages = {"English","עברית","русский"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(listLanguages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0)
                {
                    //English
                    setLocale("en");
                    recreate();
                }
                else if(which == 1)
                {
                    //Hebrew
                    setLocale("iw");
                    recreate();
                }
                else if(which == 2)
                {
                    //Russian
                    setLocale("ru");
                    recreate();
                }

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setLocale(String language)
    {
        String languageToLoad = language;
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
    }

}
