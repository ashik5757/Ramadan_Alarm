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

public class AlarmService_Sehri extends Service {

    Handler handler;
    String defCity = "";


    @Override
    public void onCreate() {
        super.onCreate();
//        pushNotification(AlarmService.this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // create new alarm receiver and setup music


        Toast.makeText(this, "Sehri Notification Started", Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        defCity = sp.getString("CITY","NONE");

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM,YYYY");
        String todayDate = dateFormat.format(currentDate);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
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
//        LocalTime diffIfter = sTime.minusHours(currentTime.getHour()).minusMinutes(currentTime.getMinute()).minusSeconds(currentTime.getSecond());



        Calendar cIfter = Calendar.getInstance();
        cIfter.setTimeInMillis(System.currentTimeMillis());
        cIfter.set(Calendar.HOUR_OF_DAY, sTime.getHour());
        cIfter.set(Calendar.MINUTE, sTime.getMinute());
        cIfter.set(Calendar.SECOND, 0);

        long one_min = 60*1000;
        long hr = cIfter.getTimeInMillis()-(60*one_min);
        long min_30 = cIfter.getTimeInMillis()-(30*one_min);
        long min_15 = cIfter.getTimeInMillis()-(45*one_min);
        long min_5 = cIfter.getTimeInMillis()-(55*one_min);
        long min_0 = cIfter.getTimeInMillis();

        if (currentTime.isBefore(sTime)) {

            if (System.currentTimeMillis()<hr)
                setUpAlarm(4001, hr);
            else if (System.currentTimeMillis()<hr)
                setUpAlarm(4002, min_30);
            else if (System.currentTimeMillis()<min_30)
                setUpAlarm(4003, min_15);
            else if (System.currentTimeMillis()<min_5)
                setUpAlarm(4004, min_5);
            else if (System.currentTimeMillis()<min_0)
                setUpAlarm(4005, min_0);
        }


//        if (System.currentTimeMillis()>=hr && System.currentTimeMillis()<=(min_0+3000)) {
//            pushNotification(AlarmService_Sehri.this, diffIfter);
//            if (System.currentTimeMillis()==min_5)
//                pushNotification(AlarmService_Sehri.this, diffIfter);
//        }


//        startForegroundWithNotification(sTime);
//
//        handler = new Handler(Looper.getMainLooper());
//        LocalTime finalSTime = sTime;
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                LocalTime currentTime = LocalTime.now();
//
//                LocalTime diffSehri = finalSTime.minusHours(currentTime.getHour()).minusMinutes(currentTime.getMinute()).minusSeconds(currentTime.getSecond());
//
//
//                if (currentTime.isBefore(finalSTime)){
//                    if (diffSehri.getHour()==1 && diffSehri.getMinute()==0 && diffSehri.getSecond()==1)
//                        pushNotification(AlarmService_Sehri.this, diffSehri);
//                    else if (diffSehri.getHour()==0 && diffSehri.getMinute()==30 && diffSehri.getSecond()==1)
//                        pushNotification(AlarmService_Sehri.this, diffSehri);
//                    else if (diffSehri.getHour()==0 && diffSehri.getMinute()==15 && diffSehri.getSecond()==1)
//                        pushNotification(AlarmService_Sehri.this, diffSehri);
//                    else if (diffSehri.getHour()==0 && diffSehri.getMinute()==5 && diffSehri.getSecond()==1)
//                        pushNotification(AlarmService_Sehri.this, diffSehri);
//                    else if (diffSehri.getHour()==0 && diffSehri.getMinute()==0 && diffSehri.getSecond()==1)
//                        pushNotification(AlarmService_Sehri.this, diffSehri);
//
//                }
//
//                else if (currentTime.isAfter(finalSTime)) {
//                    setUpNextSehriAlarm();
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
        Intent intentIfter = new Intent(this, AlarmAt_Sehri.class);
        PendingIntent pendingIntentIfter = PendingIntent.getBroadcast(this, REQ_CODE, intentIfter, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millSec, pendingIntentIfter);
    }


    private void startForegroundWithNotification(LocalTime sTime) {

        String hr = String.valueOf(sTime.getHour());
        String min = String.valueOf(sTime.getMinute());
        hr = numberConvertor(hr);
        min = numberConvertor(min);
        String seheri_time = hr+" টা "+min+" মিনিট";

        Intent intent = new Intent(this, LauncherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,11001,intent,PendingIntent.FLAG_IMMUTABLE);

        final String CHANNELID = "Sehri Foreground Service";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setSubText("Running")
                .setContentTitle("[" + defCity + "] -আজকের সেহেরির সময় : "+ seheri_time)
                .setSmallIcon(R.drawable.decoration)
                .setContentIntent(pendingIntent);

        startForeground(10001,notification.build());
    }





    private void setUpNextSehriAlarm() {
        Calendar cSehri = Calendar.getInstance();
        cSehri.setTimeInMillis(System.currentTimeMillis());
        cSehri.set(Calendar.HOUR_OF_DAY, 3);
        cSehri.set(Calendar.MINUTE, 30);
        cSehri.set(Calendar.SECOND, 0);
        cSehri.add(Calendar.DAY_OF_MONTH, 1);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intentSehri = new Intent(this, AlarmReciever_Sehri.class);
        PendingIntent pendingIntentSehri = PendingIntent.getBroadcast(this, 4, intentSehri, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cSehri.getTimeInMillis(), pendingIntentSehri);

    }

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

        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.decoration,null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("Sehri", "Sehri Notification", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        Intent intent = new Intent(this, LauncherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,9999,intent,PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "Sehri")
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        Toast.makeText(this, "Sehri Notification Stopped", Toast.LENGTH_SHORT).show();
//        pushNotification(AlarmService.this);

    }
}
