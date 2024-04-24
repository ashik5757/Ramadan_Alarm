package com.example.ramadanalarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class AlarmService_Ifter extends Service {

    Handler handler;
    String defCity = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // create new alarm receiver and setup music

        Toast.makeText(this, "Ifter Notification Started", Toast.LENGTH_SHORT).show();



        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        defCity = sp.getString("CITY","NONE");


        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM,YYYY");
        String todayDate = dateFormat.format(currentDate);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        RamadanTime ramadanTime = databaseHelper.getTodaysTime(todayDate);
        String ifter_time = ramadanTime.getIfterTime();
        LocalTime iTime = LocalTime.parse(ifter_time, DateTimeFormatter.ofPattern("HH:mm:ss"));

        if (defCity.equals("নওগাঁ")) {
            iTime = iTime.plusMinutes(6);
        }

        else if (defCity.equals("চট্টগ্রাম")) {
           iTime = iTime.minusMinutes(6);
        }


        LocalTime currentTime = LocalTime.now();
        LocalTime diffIfter = iTime.minusHours(currentTime.getHour()).minusMinutes(currentTime.getMinute()).minusSeconds(currentTime.getSecond());



        Calendar cIfter = Calendar.getInstance();
        cIfter.setTimeInMillis(System.currentTimeMillis());
        cIfter.set(Calendar.HOUR_OF_DAY, iTime.getHour());
        cIfter.set(Calendar.MINUTE, iTime.getMinute());
        cIfter.set(Calendar.SECOND, 0);

        long one_min = 60*1000;
        long hr = cIfter.getTimeInMillis()-(60*one_min);
        long min_30 = cIfter.getTimeInMillis()-(30*one_min);
        long min_15 = cIfter.getTimeInMillis()-(45*one_min);
        long min_5 = cIfter.getTimeInMillis()-(55*one_min);
        long min_0 = cIfter.getTimeInMillis();

        if (currentTime.isBefore(iTime)) {

            if (System.currentTimeMillis()<hr)
                setUpAlarm(6001, hr);
            else if (System.currentTimeMillis()<hr)
                setUpAlarm(6002, min_30);
            else if (System.currentTimeMillis()<min_30)
                setUpAlarm(6003, min_15);
            else if (System.currentTimeMillis()<min_5)
                setUpAlarm(6004, min_5);
            else if (System.currentTimeMillis()<min_0)
                setUpAlarm(6005, min_0);
        }


//        if (System.currentTimeMillis()>=hr && System.currentTimeMillis()<=(min_0+3000)) {
//            pushNotification(AlarmService_Ifter.this, diffIfter);
//            if (System.currentTimeMillis()==min_5)
//                pushNotification(AlarmService_Ifter.this, diffIfter);
//        }



//        startForegroundWithNotification(iTime);

//        handler = new Handler(Looper.getMainLooper());
//        LocalTime finalITime = iTime;
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                LocalTime currentTime = LocalTime.now();
//
//                LocalTime diffIfter = finalITime.minusHours(currentTime.getHour()).minusMinutes(currentTime.getMinute()).minusSeconds(currentTime.getSecond());
//
//
//
//                if (currentTime.isBefore(finalITime)) {
//                    if (diffIfter.getHour()==11 && diffIfter.getMinute()==48 && diffIfter.getSecond()==1)
//                        pushNotification(AlarmService_Ifter.this, diffIfter);
//                    else if (diffIfter.getHour()==0 && diffIfter.getMinute()==30 && diffIfter.getSecond()==1)
//                        pushNotification(AlarmService_Ifter.this, diffIfter);
//                    else if (diffIfter.getHour()==0 && diffIfter.getMinute()==15 && diffIfter.getSecond()==1)
//                        pushNotification(AlarmService_Ifter.this, diffIfter);
//                    else if (diffIfter.getHour()==0 && diffIfter.getMinute()==5 && diffIfter.getSecond()==1)
//                        pushNotification(AlarmService_Ifter.this, diffIfter);
//                    else if (diffIfter.getHour()==0 && diffIfter.getMinute()==0 && diffIfter.getSecond()==1)
//                        pushNotification(AlarmService_Ifter.this, diffIfter);
//                }
//
//                else if (currentTime.isAfter(finalITime)) {
//                    setUpNextIfterAlarm();
//                    handler.removeCallbacks(this);
//                    onDestroy();
//                }
//
//
//                handler.postDelayed(this,0);
//            }
//        };
//        handler.postDelayed(runnable,0);



        return START_STICKY;
    }


    public void setUpAlarm(int REQ_CODE, long millSec) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intentIfter = new Intent(this, AlarmAt_Ifter.class);
        PendingIntent pendingIntentIfter = PendingIntent.getBroadcast(this, REQ_CODE, intentIfter, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millSec, pendingIntentIfter);
    }

    private void startForegroundWithNotification(LocalTime iTime) {

        iTime = iTime.minusHours(12);
        String hr = String.valueOf(iTime.getHour());
        String min = String.valueOf(iTime.getMinute());
        hr = numberConvertor(hr);
        min = numberConvertor(min);
        String ifter_time = hr+" টা "+min+" মিনিট";

        Intent intent = new Intent(this, LauncherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,22001,intent,PendingIntent.FLAG_IMMUTABLE);

        final String CHANNELID = "Ifter Foreground Service";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setSubText("Running")
                .setContentTitle("[" + defCity + "] - আজকের ইফতারের সময় : "+ ifter_time)
                .setSmallIcon(R.drawable.decoration)
                .setContentIntent(pendingIntent);

        startForeground(20001,notification.build());
    }

    private void setUpNextIfterAlarm(int REQ_CODE) {

        Calendar cIfter = Calendar.getInstance();
        cIfter.setTimeInMillis(System.currentTimeMillis());
        cIfter.set(Calendar.HOUR_OF_DAY, 17);
        cIfter.set(Calendar.MINUTE, 0);
        cIfter.set(Calendar.SECOND, 0);
        cIfter.add(Calendar.DAY_OF_MONTH, 1);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intentIfter = new Intent(this, AlarmReciever_Ifter.class);
        PendingIntent pendingIntentIfter = PendingIntent.getBroadcast(this, 6, intentIfter, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cIfter.getTimeInMillis(), pendingIntentIfter);


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void pushNotification(Context context, LocalTime diff) {

        String hr = String.valueOf(diff.getHour());
        hr = numberConvertor(hr);
        String min = String.valueOf(diff.getMinute());
        min = numberConvertor(min);

        String remTime = "";
        if (diff.getHour()==0 && diff.getMinute()==0 && diff.getSecond()==0)
            remTime = "ইফতারের সময় হয়েছে ";
        else if (diff.getHour()==0)
            remTime = "[" + defCity + "] - আর মাত্র " + min + " মিনিট বাকি";
        else
            remTime = "[" + defCity + "] - আর মাত্র " + hr + " ঘন্টা বাকি";



        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.decoration,null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("Ifter", "Ifter Notification", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        Intent intent = new Intent(this, LauncherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,9999,intent,PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "Ifter")
                .setSmallIcon(R.drawable.decoration)
                .setLargeIcon(largeIcon)
                .setSubText("Ifter Time Alert")
                .setContentTitle(remTime)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        notificationManager.notify(2000,notification.build());


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


    @Override
    public void onDestroy() {
        super.onDestroy();
//        handler.removeCallbacksAndMessages(null);
        Toast.makeText(this, "Ifter Notification Stopped", Toast.LENGTH_SHORT).show();
    }


}
