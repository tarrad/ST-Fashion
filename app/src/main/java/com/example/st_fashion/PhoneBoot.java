package com.example.st_fashion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneBoot extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            Intent i = new Intent(context,AlarmService.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(i);
        }
    }

}