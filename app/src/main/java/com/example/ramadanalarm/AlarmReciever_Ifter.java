package com.example.ramadanalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReciever_Ifter extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Ifter OnReceive Started", Toast.LENGTH_SHORT).show();

        Intent intentService = new Intent(context, AlarmService_Ifter.class);
        context.startService(intentService);



    }
}
