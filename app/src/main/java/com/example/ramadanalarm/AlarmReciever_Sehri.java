package com.example.ramadanalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class AlarmReciever_Sehri extends BroadcastReceiver {

    private static final int DELAY_TIME = 3000;
    String defCity = "";

    @Override
    public void onReceive(Context context, Intent intent) {


        Toast.makeText(context, "Sehri OnReceive Started", Toast.LENGTH_SHORT).show();

        Intent intentService = new Intent(context, AlarmService_Sehri.class);
        context.startService(intentService);




//        SharedPreferences sp = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE);
//        defCity = sp.getString("CITY","NONE");
//
//
//        Calendar calendar = Calendar.getInstance();
//        Date currentDate = calendar.getTime();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM,YYYY");
//        String todayDate = dateFormat.format(currentDate);
//
//        DatabaseHelper databaseHelper = new DatabaseHelper(context);
//        RamadanTime ramadanTime = databaseHelper.getTodaysTime(todayDate);
//        String ifter_time = ramadanTime.getIfterTime();
//        LocalTime iTime = LocalTime.parse(ifter_time, DateTimeFormatter.ofPattern("HH:mm:ss"));
//
//        if (defCity.equals("নওগাঁ")) {
//            iTime = iTime.plusMinutes(6);
//        }
//
//        else if (defCity.equals("চট্টগ্রাম")) {
//            iTime = iTime.minusMinutes(6);
//        }
//
//
//        LocalTime currentTime = LocalTime.now();
//        LocalTime diffIfter = iTime.minusHours(currentTime.getHour()).minusMinutes(currentTime.getMinute()).minusSeconds(currentTime.getSecond());
//
//
//
//        Calendar cIfter = Calendar.getInstance();
//        cIfter.setTimeInMillis(System.currentTimeMillis());
//        cIfter.set(Calendar.HOUR_OF_DAY, iTime.getHour());
//        cIfter.set(Calendar.MINUTE, iTime.getMinute());
//        cIfter.set(Calendar.SECOND, 0);
//
//        long one_min = 60*1000;
//        long hr = cIfter.getTimeInMillis()-(60*one_min);
//        long min_30 = cIfter.getTimeInMillis()-(30*one_min);
//        long min_15 = cIfter.getTimeInMillis()-(45*one_min);
//        long min_5 = cIfter.getTimeInMillis()-(55*one_min);
//        long min_0 = cIfter.getTimeInMillis();
//
//        if (currentTime.isBefore(iTime)) {
//
//            if (System.currentTimeMillis()<hr)
//                setUpAlarm(6001, hr);
//            else if (System.currentTimeMillis()<hr)
//                setUpAlarm(6002, min_30);
//            else if (System.currentTimeMillis()<min_30)
//                setUpAlarm(6003, min_15);
//            else if (System.currentTimeMillis()<min_5)
//                setUpAlarm(6004, min_5);
//            else if (System.currentTimeMillis()<min_0)
//                setUpAlarm(6005, min_0);
//        }





    }

    public void setUpAlarm(Context context, int REQ_CODE, long millSec) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentIfter = new Intent(context, AlarmReciever_Sehri.class);
        PendingIntent pendingIntentIfter = PendingIntent.getBroadcast(context, REQ_CODE, intentIfter, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millSec, pendingIntentIfter);
    }

}
