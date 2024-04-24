package com.example.ramadanalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class Settings extends AppCompatActivity implements View.OnClickListener {

    String[] sehri = {"নেই","১ ঘণ্টা ৩০ মিনিট আগে", "১ ঘণ্টা আগে", "৩০ মিনিট আগে", "১৫ মিনিট আগে"};
    String[] iftar = {"নেই","১ ঘণ্টা আগে", "৩০ মিনিট আগে", "১৫ মিনিট আগে"};

    Toolbar toolbar;
    MaterialSwitch switch_sehri, switch_iftar;
    TextInputLayout tiL_alarm_sehri, tiL_alarm_iftar;
    Button bt_apply;
    AutoCompleteTextView select_time_sehri, select_time_iftar;

    ArrayAdapter<String> arrayAdapter_sehri;
    ArrayAdapter<String> arrayAdapter_iftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Typeface tf_charu_regular = Typeface.createFromAsset(getAssets(), "font/Charu_Chandan_Unicode-Regular.ttf");
        Typeface tf_charu_bold = Typeface.createFromAsset(getAssets(), "font/Charu_Chandan_Unicode-Bold.ttf");

        switch_sehri = findViewById(R.id.switch_sehri);
        switch_iftar = findViewById(R.id.switch_iftar);
        switch_sehri.setTypeface(tf_charu_bold);
        switch_iftar.setTypeface(tf_charu_bold);

        tiL_alarm_sehri = findViewById(R.id.tiL_alarm_sehri);
        tiL_alarm_iftar = findViewById(R.id.tiL_alarm_iftar);
        tiL_alarm_sehri.setTypeface(tf_charu_regular);
        tiL_alarm_iftar.setTypeface(tf_charu_regular);

        select_time_sehri = findViewById(R.id.select_time_sehri);
        select_time_iftar = findViewById(R.id.select_time_ifatr);

        bt_apply = findViewById(R.id.bt_apply);

        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        String before_sehri = sp.getString("ALARM_AT_BEFORE_SEHRI", "নেই");
        String before_iftar = sp.getString("ALARM_AT_BEFORE_IFTAR", "নেই");
        boolean at_sehri = sp.getBoolean("AT_SEHRI", false);
        boolean at_iftar = sp.getBoolean("AT_IFTAR", false);

        select_time_sehri.setText(before_sehri);
        select_time_iftar.setText(before_iftar);
        switch_sehri.setChecked(at_sehri);
        switch_iftar.setChecked(at_iftar);


        arrayAdapter_sehri = new ArrayAdapter<>(this,R.layout.city_list,sehri);
        arrayAdapter_iftar = new ArrayAdapter<>(this,R.layout.city_list,iftar);

        select_time_sehri.setAdapter(arrayAdapter_sehri);
        select_time_sehri.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        select_time_iftar.setAdapter(arrayAdapter_iftar);
        select_time_iftar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });



        switch_sehri.setOnClickListener(this);
        switch_iftar.setOnClickListener(this);


        bt_apply.setVisibility(View.INVISIBLE);


    }


    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.switch_sehri)
            setAlarmAtSehri();
        if (v.getId()==R.id.switch_iftar)
            setAlarmAtIftar();

    }

    private void setAlarmAtSehri() {

        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        if (switch_sehri.isChecked()) {
            scheduleAlarm(this,4);
            e.putBoolean("AT_SEHRI", true);
            e.commit();
        }

        else if (!switch_sehri.isChecked()) {
            cancelAlarm(this, 4);
            e.putBoolean("AT_SEHRI", false);
            e.commit();
        }

    }


    private void setAlarmAtIftar() {

        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        if (switch_iftar.isChecked()) {
            scheduleAlarm(this,6);
            e.putBoolean("AT_IFTAR", true);
            e.commit();
        }

        else if (!switch_iftar.isChecked()) {
            cancelAlarm(this,6);
            e.putBoolean("AT_IFTAR", false);
            e.commit();
        }


    }




    public void scheduleAlarm(Context context, int REQ_CODE) {

//        String iftar_time = "";
//        String sehri_time = "";
//        boolean isSehriNextDay = false;
//
//        Intent tIntent = getIntent();
//        if (tIntent!=null){
//            sehri_time = tIntent.getStringExtra("SEHRI_TIME");
//            iftar_time = tIntent.getStringExtra("IFTAR_TIME");
//            isSehriNextDay = tIntent.getBooleanExtra("IS_SEHRI_NEXTDAY",false);
//        }
//
//        LocalDate date = LocalDate.now();
//        if (isSehriNextDay)
//            date = date.plusDays(1);
//
//        LocalTime iTime = LocalTime.parse(iftar_time, DateTimeFormatter.ofPattern("HH:mm:ss"));
//        LocalTime sTime = LocalTime.parse(sehri_time, DateTimeFormatter.ofPattern("HH:mm:ss"));
//        LocalDateTime iDateTime = LocalDateTime.of(date,iTime);
//        LocalDateTime sDateTime = LocalDateTime.of(date,sTime);

/////////////////////////////////////////////////////////////////////////

        SharedPreferences sp = getSharedPreferences("user_settings", MODE_PRIVATE);
        String defCity = sp.getString("CITY","NONE");



        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM,YYYY");
        String todayDate = dateFormat.format(currentDate);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        RamadanTime ramadanTime = databaseHelper.getTodaysTime(todayDate);
        String ifter_time = ramadanTime.getIfterTime();
        String sehri_time = ramadanTime.getSehriTime();

        LocalTime iTime = LocalTime.parse(ifter_time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime sTime = LocalTime.parse(sehri_time, DateTimeFormatter.ofPattern("HH:mm:ss"));;

        if (defCity.equals("নওগাঁ")) {
            iTime = iTime.plusMinutes(6);
            sTime = sTime.plusMinutes(5);
        }

        else if (defCity.equals("চট্টগ্রাম")) {
            iTime = iTime.minusMinutes(6);
            sTime = sTime.minusMinutes(5);
        }


        LocalTime currentTime = LocalTime.now();



        if (REQ_CODE==4) {
            Calendar cSehri = Calendar.getInstance();
            cSehri.setTimeInMillis(System.currentTimeMillis());
            cSehri.set(Calendar.HOUR_OF_DAY, 3);
            cSehri.set(Calendar.MINUTE, 30);
            cSehri.set(Calendar.SECOND, 0);

            if (cSehri.getTimeInMillis() <= System.currentTimeMillis() && currentTime.isBefore(sTime)) {
                Intent intentService = new Intent(context, AlarmService_Sehri.class);
                context.startService(intentService);
                return;
            }

            else if (cSehri.getTimeInMillis() <= System.currentTimeMillis() && currentTime.isAfter(sTime)) {
                cSehri.add(Calendar.DAY_OF_MONTH, 1);
            }

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intentSehri = new Intent(context, AlarmReciever_Sehri.class);
            PendingIntent pendingIntentSehri = PendingIntent.getBroadcast(context, REQ_CODE, intentSehri, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cSehri.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentSehri);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cSehri.getTimeInMillis(), pendingIntentSehri);


        }

        else if (REQ_CODE==6) {
            Calendar cIfter = Calendar.getInstance();
            cIfter.setTimeInMillis(System.currentTimeMillis());
            cIfter.set(Calendar.HOUR_OF_DAY, 17);
            cIfter.set(Calendar.MINUTE, 0);
            cIfter.set(Calendar.SECOND, 0);


            if (cIfter.getTimeInMillis() <= System.currentTimeMillis() && currentTime.isBefore(iTime)) {
                Intent intentService = new Intent(context, AlarmService_Ifter.class);
                context.startService(intentService);
                return;
            }

            else if (cIfter.getTimeInMillis() < System.currentTimeMillis() && currentTime.isAfter(iTime)) {
                cIfter.add(Calendar.DAY_OF_MONTH, 1);
            }



            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intentIfter = new Intent(context, AlarmReciever_Ifter.class);
            PendingIntent pendingIntentIfter = PendingIntent.getBroadcast(context, REQ_CODE, intentIfter, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cIfter.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentIfter);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cIfter.getTimeInMillis(), pendingIntentIfter);




        }


    }

    public void setUpAlarm(int REQ_CODE, long millSec) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intentIfter = new Intent(this, AlarmReciever_Ifter.class);
        PendingIntent pendingIntentIfter = PendingIntent.getBroadcast(this, REQ_CODE, intentIfter, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, millSec, pendingIntentIfter);
    }


    public void cancelAlarm(Context context, int REQ_CODE) {

        if (REQ_CODE==4) {
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            Intent intent = new Intent(context, AlarmReciever_Sehri.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQ_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
//
//            alarmManager.cancel(pendingIntent);
//            pendingIntent.cancel();
//
//            Intent intentService = new Intent(context, AlarmService_Sehri.class);
//            context.stopService(intentService);


            calcelSehriAlarm(4);
            calcelSehriAlarm(4001);
            calcelSehriAlarm(4002);
            calcelSehriAlarm(4003);
            calcelSehriAlarm(4004);
            calcelSehriAlarm(4005);


        }

        else if (REQ_CODE==6) {
            calcelIfterAlarm(6);
            calcelIfterAlarm(6001);
            calcelIfterAlarm(6002);
            calcelIfterAlarm(6003);
            calcelIfterAlarm(6004);
            calcelIfterAlarm(6005);
        }

    }

    public void calcelIfterAlarm(int REQ_CODE) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmAt_Ifter.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQ_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent!=null && alarmManager!=null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

    }


    public void calcelSehriAlarm(int REQ_CODE) {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmAt_Sehri.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQ_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent!=null && alarmManager!=null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

    }





}