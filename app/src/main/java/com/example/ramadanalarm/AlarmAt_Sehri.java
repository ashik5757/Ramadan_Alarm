package com.example.ramadanalarm;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class AlarmAt_Sehri extends BroadcastReceiver {

    String defCity = "";

    @Override
    public void onReceive(Context context, Intent intent) {

//        Toast.makeText(context, "Ifter Notification Started", Toast.LENGTH_SHORT).show();


        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM,YYYY");
        String todayDate = dateFormat.format(currentDate);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        RamadanTime ramadanTime = databaseHelper.getTodaysTime(todayDate);
        String sehri_time = ramadanTime.getSehriTime();
        LocalTime sTime = LocalTime.parse(sehri_time, DateTimeFormatter.ofPattern("HH:mm:ss"));

        if (defCity.equals("নওগাঁ")) {
            sTime = sTime.plusMinutes(5);
        }

        else if (defCity.equals("চট্টগ্রাম")) {
            sTime = sTime.minusMinutes(5);
        }



        LocalTime currentTime = LocalTime.now();
        LocalTime diffIfter = sTime.minusHours(currentTime.getHour()).minusMinutes(currentTime.getMinute()).minusSeconds(currentTime.getSecond());

        pushNotification(context, diffIfter);


    }

//    public void setUpAlarm(Context context, int REQ_CODE, long millSec) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intentIfter = new Intent(context, AlarmReciever_Sehri.class);
//        PendingIntent pendingIntentIfter = PendingIntent.getBroadcast(context, REQ_CODE, intentIfter, PendingIntent.FLAG_IMMUTABLE);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millSec, pendingIntentIfter);
//    }
//
//
//    private void setUpNextSehriAlarm(Context context) {
//        Calendar cSehri = Calendar.getInstance();
//        cSehri.setTimeInMillis(System.currentTimeMillis());
//        cSehri.set(Calendar.HOUR_OF_DAY, 3);
//        cSehri.set(Calendar.MINUTE, 30);
//        cSehri.set(Calendar.SECOND, 0);
//        cSehri.add(Calendar.DAY_OF_MONTH, 1);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intentSehri = new Intent(context, AlarmReciever_Sehri.class);
//        PendingIntent pendingIntentSehri = PendingIntent.getBroadcast(context, 4, intentSehri, PendingIntent.FLAG_IMMUTABLE);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cSehri.getTimeInMillis(), pendingIntentSehri);
//
//    }


    private void pushNotification(Context context, LocalTime diff) {

        String hr = String.valueOf(diff.getHour());
        hr = numberConvertor(hr);
        String min = String.valueOf(diff.getMinute());
        min = numberConvertor(min);


        String remTime = "";
        if (diff.getHour()==0 && diff.getMinute()==0 && diff.getSecond()==1)
            remTime = "সেহেরির সময় হয়েছে ";
        else if (diff.getHour()==0)
            remTime = "[" + defCity + "] - আর মাত্র " + min + " মিনিট বাকি";
        else
            remTime = "[" + defCity + "] - আর মাত্র " + hr + " ঘন্টা বাকি";

        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),R.drawable.decoration,null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("Sehri", "Sehri Notification", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        Intent intent = new Intent(context, LauncherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,9999,intent,PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "Sehri")
                .setSmallIcon(R.drawable.decoration)
                .setLargeIcon(largeIcon)
                .setSubText("Sehri Time Alert")
                .setContentText(remTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(1000,notification.build());

        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        },3000);


    }

    private String numberConvertor(String digit){
        digit = digit.replace("0","০");
        digit = digit.replace("1","১");
        digit = digit.replace("2","২");
        digit = digit.replace("3","৩");
        digit = digit.replace("4","৪");
        digit = digit.replace("5","৫");
        digit = digit.replace("6","৬");
        digit = digit.replace("7","৭");
        digit = digit.replace("8","৮");
        digit = digit.replace("9","৯");
        return digit;
    }
}
